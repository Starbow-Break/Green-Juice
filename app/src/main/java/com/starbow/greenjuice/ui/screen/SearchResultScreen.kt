package com.starbow.greenjuice.ui.screen

import android.content.res.Configuration
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.starbow.greenjuice.R
import com.starbow.greenjuice.data.SampleDataSource
import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceStatistics
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.model.SentimentStatistics
import com.starbow.greenjuice.ui.GreenJuiceNetworkUiState
import com.starbow.greenjuice.ui.theme.*

//검색 결과를 띄우는 화면
@Composable
fun SearchResultScreen(
    netUiState: GreenJuiceNetworkUiState,
    amountOfItem: Int,
    juiceStatistics: JuiceStatistics,
    sentimentStatistics: SentimentStatistics,
    searchItems: List<JuiceItem>,
    modifier: Modifier = Modifier,
    query: String = "",
    showAddButton: Boolean = true,
    onFilterClicked: () -> Unit = {},
    onChangeQuery: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onClearQuery: () -> Unit = {},
    onAddDataClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        SearchBar(
            query = query,
            onChangeQuery = onChangeQuery,
            onSearch = onSearch,
            onClearQuery = onClearQuery
        )
        FilterButtonView(onFilterClicked = onFilterClicked)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (netUiState) {
                GreenJuiceNetworkUiState.Loading ->
                    LoadingScreen()
                GreenJuiceNetworkUiState.Error ->
                    NetworkErrorScreen()
                else -> {
                    SearchResultView(
                        amountOfItem = amountOfItem,
                        juiceStatistics = juiceStatistics,
                        sentimentStatistics = sentimentStatistics,
                        searchItems = searchItems,
                        addDataLoading = (netUiState is GreenJuiceNetworkUiState.LoadingAdditional),
                        onAddDataClick = onAddDataClick,
                        showAddButton = showAddButton,
                        onCardClick = onCardClick,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

//통계 정보를 보여주는 부분
@Composable
fun StatisticsView(
    amountOfItem: Int,
    juiceStatistics: JuiceStatistics,
    sentimentStatistics: SentimentStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            SearchItemAmountView(amountOfItem)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatisticsTitleAndDonutChartView(
                    title = R.string.juice_title,
                    amountOfItem = amountOfItem,
                    dataMap = mapOf(
                        stringResource(id = R.string.green) to Pair(juiceStatistics.numberOfGreen, JuiceColor.GREEN.color),
                        stringResource(id = R.string.orange) to Pair(juiceStatistics.numberOfOrange, JuiceColor.ORANGE.color),
                        stringResource(id = R.string.red) to Pair(juiceStatistics.numberOfRed, JuiceColor.RED.color),
                    )
                )
                StatisticsTitleAndDonutChartView(
                    title = R.string.sentiment_title,
                    amountOfItem = amountOfItem,
                    dataMap = mapOf(
                        stringResource(id = Sentiment.POSITIVE.stringRes) to Pair(sentimentStatistics.numberOfPositive, Sentiment.POSITIVE.color),
                        stringResource(id = Sentiment.NEUTRAL.stringRes) to Pair(sentimentStatistics.numberOfNeutrality, Sentiment.NEUTRAL.color),
                        stringResource(id = Sentiment.NEGATIVE.stringRes) to Pair(sentimentStatistics.numberOfNegative, Sentiment.NEGATIVE.color),
                    )
                )
            }
        }
    }
}

//로딩 중 화면 (새 검색어로 검색 및 추가 데이터 받아올 때 사용)
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

//네트워크 에러 시 화면
@Composable
fun NetworkErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = stringResource(id = R.string.network_main_error))
    }
}

//검색 결과로 얻은 리뷰 수
@Composable
fun SearchItemAmountView(
    amount: Int,
    modifier: Modifier = Modifier
) {
    Text(text = stringResource(id = R.string.search_item_amount, amount), modifier = modifier)
}

//통계 제목 및 도넛 모양 그래프
@Composable
fun StatisticsTitleAndDonutChartView(
    @StringRes title: Int,
    amountOfItem: Int,
    dataMap: Map<String, Pair<Int, Color>>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = title),
            style = Typography.h3
        )
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 160.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            DonutProgress(
                model = DonutModel(
                    cap = amountOfItem.toFloat(),
                    masterProgress = 1f,
                    gapWidthDegrees = 50f,
                    gapAngleDegrees = 90f,
                    strokeWidth = 20f,
                    backgroundLineColor = Color.LightGray,
                    sections = dataMap.toList().map { data ->
                        DonutSection(amount = data.second.first.toFloat(), color = data.second.second)
                    }
                ),
                modifier = Modifier.fillMaxSize()
            )
            Column {
                dataMap.forEach { data ->
                    ValueIndicator(title = data.key, value = data.value.first, color = data.value.second)
                }
            }
        }
    }
}

//리뷰 분석 결과값을 보여주는 뷰
@Composable
fun ValueIndicator(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.statistics_indicator_description, title, value),
            fontSize = 10.sp
        )
    }
}

//검색 결과를 보여주는 부분
@Composable
fun SearchResultView(
    amountOfItem: Int,
    juiceStatistics: JuiceStatistics,
    sentimentStatistics: SentimentStatistics,
    searchItems: List<JuiceItem>,
    showAddButton: Boolean,
    addDataLoading: Boolean,
    onAddDataClick: () -> Unit,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if(searchItems.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = stringResource(id = R.string.empty_search_item_message))
            }
        }
        else {
            LazyColumn(
                contentPadding = PaddingValues(4.dp)
            ) {
                item {
                    StatisticsView(
                        amountOfItem = amountOfItem,
                        juiceStatistics = juiceStatistics,
                        sentimentStatistics = sentimentStatistics,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                /* 현재 서버가 구현되지 않아 UI를 테스트 하는 코드로 작성됨 */
                items(searchItems) { data ->
                    SearchResultItem(
                        juiceItem = data,
                        onClick = onCardClick,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                /*추가 데이터를 가져오는 버튼*/
                if(showAddButton) {
                    item {
                        AddDataButton(
                            loading = addDataLoading,
                            onClick = onAddDataClick,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddDataButton(
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(100),
        enabled = !loading,
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text(text = stringResource(id = R.string.add_data_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    GreenJuiceTheme {
        SearchResultScreen(
            netUiState = GreenJuiceNetworkUiState.Loading,
            amountOfItem = 9,
            searchItems = SampleDataSource.dataList,
            juiceStatistics = JuiceStatistics(3, 4, 2),
            sentimentStatistics = SentimentStatistics(5, 4)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NetWorkErrorPreview() {
    GreenJuiceTheme {
        SearchResultScreen(
            netUiState = GreenJuiceNetworkUiState.Error,
            amountOfItem = 9,
            searchItems = SampleDataSource.dataList,
            juiceStatistics = JuiceStatistics(3, 4, 2),
            sentimentStatistics = SentimentStatistics(5, 4)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultWindowPreview() {
    GreenJuiceTheme {
        SearchResultScreen(
            netUiState = GreenJuiceNetworkUiState.Success,
            amountOfItem = 9,
            searchItems = SampleDataSource.dataList,
            juiceStatistics = JuiceStatistics(3, 4, 2),
            sentimentStatistics = SentimentStatistics(5, 4)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultWindowWithEmptySearchItemPreview() {
    GreenJuiceTheme {
        SearchResultScreen(
            netUiState = GreenJuiceNetworkUiState.Success,
            amountOfItem = 0,
            searchItems = listOf(),
            juiceStatistics = JuiceStatistics(0, 0, 0),
            sentimentStatistics = SentimentStatistics(0, 0)
        )
    }
}

@Preview
@Composable
fun AddDataButtonNoLoadingPreview() {
    AddDataButton(loading = false, onClick = {})
}

@Preview
@Composable
fun AddDataButtonLoadingPreview() {
    AddDataButton(loading = true, onClick = {})
}

@Preview
@Composable
fun LoadingScreenPreview() {
    GreenJuiceTheme {
        LoadingScreen()
    }
}

fun String.deleteBoldTag(): String {
    return this.replace("<b>", "").replace("</b>", "")
}