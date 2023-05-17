package com.starbow.greenjuice.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.Event
import com.starbow.greenjuice.data.GreenJuicePreferencesRepository
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.enum.EventToastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SignInViewModel(
    private val greenJuiceRepository: GreenJuiceRepository,
    private val greenJuicePrefRepo: GreenJuicePreferencesRepository
) : ViewModel() {
    var isLoading = MutableStateFlow(false)
        private set

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

    //로그인
    fun signIn(id: String, password: String) {
        if(isLoading.value) {
            _showToast.value = Event(EventToastMessage.REFUSE)
            return
        }

        isLoading.value = true

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    greenJuiceRepository.signIn(id, password)
                }
                val body = result.body()
                val header = result.headers()

                if(body?.getLogin() == true) {
                    val accessToken = body.getAccessToken()
                    val refreshToken = header.get("set-cookie")
                        ?.replace("refresh=", "")
                        ?.replace("; Path=/; HttpOnly", "") ?: ""
                    Log.d("Tokens", "Access Token : $accessToken\nRefresh Token : $refreshToken")

                    greenJuicePrefRepo.saveToken(accessToken, refreshToken)
                    delay(1) //새로 생성된 토큰을 다른 곳에서도 잘 쓸 수 있도록 아주 약간의 delay 추가
                    _showToast.value = Event(EventToastMessage.SIGN_IN)
                    Log.d("Account", "Sign in Success! Account ID : $id")
                } else {
                    _showToast.value = Event(EventToastMessage.SIGN_IN_FAIL)
                }
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.SIGN_IN_ERROR)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun requestRefuse() {
        _showToast.value = Event(EventToastMessage.REFUSE)
    }
}