package com.starbow.greenjuice.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.Event
import com.starbow.greenjuice.data.GreenJuicePreferencesRepository
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.enum.EventToastMessage
import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceStatistics
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.model.SentimentStatistics
import com.starbow.greenjuice.serializable.toJuiceItem
import com.starbow.greenjuice.ui.GreenJuiceNetworkUiState
import com.starbow.greenjuice.ui.GreenJuiceUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

const val AMOUNT_DATA = 5 //요청할 때 마다 받아올 데이터 갯수
const val TAG = "NavHostViewModel"

class GreenJuiceNavHostViewModel(
    private val greenJuiceRepository: GreenJuiceRepository,
    private val greenJuicePrefRepo: GreenJuicePreferencesRepository
) : ViewModel() {
    //UI 상태
    private val _uiState = MutableStateFlow(GreenJuiceUiState())
    val uiState = _uiState.asStateFlow()

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

    //네트워크 관련 UI 상태
    var netUiState: GreenJuiceNetworkUiState by mutableStateOf(GreenJuiceNetworkUiState.Success)
        private set

    //현재 동작 중인 검색 코루틴
    private var curJob: Job? = null

    //현재 검색에 사용 중인 검색어
    private var curQuery by mutableStateOf("")
    //현재 검색바에 입력된 검색어(setter는 private로 설정)
    var inputQuery by mutableStateOf("")
        private set

    //전체 데이터
    private val dataList = mutableStateListOf<JuiceItem>()

    //즐겨찾기 데이터
    var favoritesList = mutableStateListOf<JuiceItem>()
        private set

    //필터가 적용된 데이터
    var resultList = listOf<JuiceItem>()
        private set

    var accessTokenStateFlow = greenJuicePrefRepo.accessTokenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
        private set
    var refreshTokenStateFlow = greenJuicePrefRepo.refreshTokenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
        private set

    //query 값을 재설정
    fun changeQuery(newQuery: String) {
        inputQuery = newQuery
    }

    //UI 상태 업데이트(화면 전환 제외)
    private fun updateUiState(
        amountOfItem: Int,
        adStateStatistics: JuiceStatistics,
        sentimentStatistics: SentimentStatistics
    ) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                searchQuery = inputQuery,
                numberOfItems = amountOfItem,
                juiceStatistics = adStateStatistics,
                sentimentStatistics = sentimentStatistics
            )
        }
    }



    //검색 결과 얻기 (필터도 포함된 결과)
    private fun getSearchItems() {
        val curAdStateFilterOption = _uiState.value.juiceFilterOption
        val curSentimentFilterOption = _uiState.value.sentimentFilterOption

        resultList = dataList.filter {
            //필터 조건에 맞으면 아이템 추가
            (curAdStateFilterOption == null || curAdStateFilterOption == it.juiceColor)
                && (curSentimentFilterOption == null || curSentimentFilterOption == it.sentiment)
        }.toList()
    }

    //얻은 검색 결과를 토대로 통계 데이터 획득, 얻은 후 UI 상태 업데이트
    private fun getStatisticsData() {
        var amountOfItem = 0

        var numberOfNormal = 0
        var numberOfAdDoubt = 0
        var numberOfAd = 0

        var numberOfPositive = 0
        var numberOfNeutrality = 0
        var numberOfNegative = 0

        resultList.forEach { item ->
            amountOfItem++

            when(item.juiceColor) {
                JuiceColor.GREEN -> numberOfNormal++
                JuiceColor.ORANGE -> numberOfAdDoubt++
                JuiceColor.RED -> numberOfAd++
                else -> {}
            }

            when(item.sentiment) {
                Sentiment.POSITIVE -> numberOfPositive++
                Sentiment.NEUTRAL -> numberOfNeutrality++
                Sentiment.NEGATIVE -> numberOfNegative++
                else -> {}
            }
        }

        val juiceStatistics = JuiceStatistics(numberOfNormal, numberOfAdDoubt, numberOfAd)
        val sentimentStatistics = SentimentStatistics(numberOfPositive, numberOfNeutrality, numberOfNegative)

        updateUiState(amountOfItem, juiceStatistics, sentimentStatistics)
    }

    //결과 변경 (필터가 적용된 결과를 얻고 통계 정보 수정)
    private fun changeResult() {
        getSearchItems() //필터 적용된 검색 결과 얻기
        getStatisticsData() //통계 데이터 만들기
    }

    //주어진 쿼리로 검색, 검색 후 분석된 결과를 토대로 통계 데이터 획득
    fun search() {
        if((netUiState == GreenJuiceNetworkUiState.Loading) or
            (netUiState == GreenJuiceNetworkUiState.LoadingAdditional)) {
            requestRefuse()
            return
        }

        netUiState = GreenJuiceNetworkUiState.Loading

        //동작중인 검색 코루틴이 있는 경우 해당 코루틴을 취소
        if(curJob != null) {
            curJob!!.cancel()
            curJob = null
        }

        dataList.clear()

        curJob = viewModelScope.launch {
            try {
                curQuery = inputQuery
                dataList.addAll(greenJuiceRepository.search(curQuery, 1, AMOUNT_DATA))
                changeResult()
                showAddDataButton()
                netUiState = GreenJuiceNetworkUiState.Success
            } catch(e: IOException) {
                Log.e(TAG, "error : " + e.message)
                e.stackTrace
                netUiState = GreenJuiceNetworkUiState.Error
            }
        }
    }

    //추가 데이터 가져오기
    fun getAdditionalData() {
        if((netUiState == GreenJuiceNetworkUiState.Loading) or
            (netUiState == GreenJuiceNetworkUiState.LoadingAdditional)) {
            requestRefuse()
            return
        }

        //동작중인 검색 코루틴이 있는 경우 해당 코루틴을 취소
        if(curJob != null) {
            curJob!!.cancel()
            curJob = null
        }

        netUiState = GreenJuiceNetworkUiState.LoadingAdditional
        val newDatas = mutableListOf<JuiceItem>()

        curJob = viewModelScope.launch {
            try {
                newDatas.addAll(greenJuiceRepository.search(curQuery, dataList.size+1, AMOUNT_DATA))

                if(newDatas.isEmpty()) hideAddDataButton()
                else {
                    dataList.addAll(newDatas)
                    changeResult()
                }

                inputQuery = curQuery
            } catch (e: IOException) {
                _showToast.value = Event(EventToastMessage.LOAD_DATA_ERROR) //에러 발생 시 에러 토스트를 띄우기 위해 이벤트를 보냄
            }
            finally {
                netUiState = GreenJuiceNetworkUiState.Success
            }
        }
    }

    //필터 다이얼로그 활성화
    fun filterDialogActivation() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                onFilter = true
            )
        }
    }

    //필터 다이얼로그 비활성화
    fun filterDialogDisabled() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                onFilter = false
            )
        }
    }

    //필터 상태 변경 (상태 변경하면서 필터 선택 화면도 비활성화 시켜준다)
    fun changeFilterState(adState: JuiceColor?, sentiment: Sentiment?) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                juiceFilterOption = adState,
                sentimentFilterOption = sentiment
            )
        }

        changeResult()
        filterDialogDisabled()
    }

    //추가 데이터 버튼 보여주기
    private fun showAddDataButton() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                showAddDataButton = true
            )
        }
    }

    //추가 데이터 버튼 숨기기
    private fun hideAddDataButton() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                showAddDataButton = false
            )
        }
    }

    private suspend fun getNewToken(accessToken: String, refreshToken: String) {
        try {
            val tokenResponse = greenJuiceRepository.getNewToken(accessToken, refreshToken)
            val ok = tokenResponse.body()?.getOk()
            val newAccessToken = tokenResponse.body()?.getAccessToken() ?: ""
            val newRefreshToken = tokenResponse.body()?.getRefreshToken() ?: ""

            greenJuicePrefRepo.saveToken(newAccessToken, newRefreshToken)
            Log.d("Get New Tokens!", "OK? : $ok")
            Log.d("Get New Tokens!", "Access Token : $newAccessToken\n" +
                        "Refresh Token : $newRefreshToken")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Get New Token", "토큰 획득 실패")
        }

    }

    fun loadFavorites(accessToken: String, refreshToken: String) {
        favoritesList.clear()

        viewModelScope.launch {
             try {
                 val response = greenJuiceRepository.getFavorites(accessToken, refreshToken)

                 Log.d("Use Tokens! load fav", "Access Token : $accessToken\n" +
                         "Refresh Token : $refreshToken")

                 if (response.body() != null && response.isSuccessful) {
                     favoritesList.addAll(
                         response.body()?.map { blogPostItem -> blogPostItem.toJuiceItem() }
                             ?: emptyList()
                     )
                 } else {
                     //토큰 만료 메시지를 받으면 토큰 재 발급 후 재요청
                     getNewToken(accessToken, refreshToken)
                 }
             } catch (e: IOException) {
                 _showToast.value = Event(EventToastMessage.LOAD_DATA_ERROR)
             }

        }
    }

    //특정 계정의 즐겨찾기 추가
    fun addFavorites(accessToken: String, refreshToken: String, postId: Int) {
        viewModelScope.launch {
            try {
                val response = greenJuiceRepository.addFavorites(accessToken, refreshToken, postId)

                if (response.isSuccessful) {
                    loadFavorites(accessToken, refreshToken)
                } else {
                    getNewToken(accessToken, refreshToken)
                    val newAccessToken = accessTokenStateFlow.value
                    val newRefreshToken = refreshTokenStateFlow.value
                    addFavorites(newAccessToken, newRefreshToken, postId)
                }
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.ADD_FAV_ERROR)
            }
        }
    }

    fun deleteFavorites(accessToken: String, refreshToken: String, postId: Int) {
        viewModelScope.launch {
            try {
                val response = greenJuiceRepository.deleteFavorites(accessToken, refreshToken, postId)

                if (response.isSuccessful) {
                    loadFavorites(accessToken, refreshToken)
                } else {
                    getNewToken(accessToken, refreshToken)
                    delay(100)
                    val newAccessToken = accessTokenStateFlow.value
                    val newRefreshToken = refreshTokenStateFlow.value
                    Log.d("RESTART", "restart")
                    deleteFavorites(newAccessToken, newRefreshToken, postId)
                }
            } catch (e: IOException) {
                _showToast.value = Event(EventToastMessage.DELETE_FAV_ERROR)
            }
        }
    }

    //들어온 요청을 거부
    fun requestRefuse() {
        _showToast.value = Event(EventToastMessage.REFUSE)
    }
}