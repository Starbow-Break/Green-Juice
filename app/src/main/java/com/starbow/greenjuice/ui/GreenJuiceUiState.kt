package com.starbow.greenjuice.ui

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceStatistics
import com.starbow.greenjuice.model.SentimentStatistics

sealed interface GreenJuiceNetworkUiState {
    object Success : GreenJuiceNetworkUiState
    object Loading : GreenJuiceNetworkUiState
    object Error : GreenJuiceNetworkUiState
    object LoadingAdditional : GreenJuiceNetworkUiState
}

data class GreenJuiceUiState(
    val accountId: String = "", //현재 로그인된 계정의 ID (빈 문자열이면 게스트 모드인 상태)
    val searchQuery: String = "", //검색 쿼리
    val onFilter: Boolean = false, //필터 기능 사용 여부
    val juiceFilterOption: JuiceColor? = null, //광고 여부 필터
    val sentimentFilterOption: Sentiment? = null, //감성 필터
    val numberOfItems: Int = 0, //검색 결과 수
    val juiceStatistics: JuiceStatistics = JuiceStatistics(), //광고 관련 통계
    val sentimentStatistics: SentimentStatistics = SentimentStatistics(), //감성 관련 통계
    val showAddDataButton: Boolean = true //더 보기 버튼 활성화 여부
)