package com.starbow.greenjuice.ui.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onAddDataClick: () -> Unit = {}
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
                    SearchResultItem(juiceItem = data, modifier = Modifier.padding(4.dp))
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

//검색 결과로 나오는 블로그 글들의 분석 결과를 보여주기 위한 리스트 아이템
@Composable
fun SearchResultItem(
    juiceItem: JuiceItem,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                JuiceResultView(juiceColor = juiceItem.juiceColor)
                Spacer(modifier = Modifier.width(8.dp))
                Column{
                    Text(text = juiceItem.title.deleteBoldTag(), style = Typography.h3)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = juiceItem.description.deleteBoldTag())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SentimentPowerLinkResultView(
                sentiment = juiceItem.sentiment,
                hasPowerLink = juiceItem.hasPowerLink
            )
            Spacer(modifier = Modifier.height(16.dp))
            KeywordsView(juiceItem.hashtags)
        }
    }
}

@Composable
fun JuiceResultView(
    juiceColor: JuiceColor,
    modifier: Modifier = Modifier
) {
    JuiceResultViewItem(
        juiceColor = juiceColor,
        text = stringResource(id = R.string.juice_with_color, stringResource(id = juiceColor.stringRes)),
        modifier = modifier.fillMaxHeight()
    )
}

@Composable
fun JuiceResultViewItem(
    juiceColor: JuiceColor,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(64.dp)
    ) {
        Image(
            painter = painterResource(id = juiceColor.imgRes),
            contentDescription = text,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .size(48.dp)
        )
        Text(text = text, color = juiceColor.color, textAlign = TextAlign.Center)
    }
}

//광고성 여부, 긍/부정 여부를 보여주는 컴포저블
@Composable
fun SentimentPowerLinkResultView(
    sentiment: Sentiment,
    hasPowerLink: Boolean,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            SentimentToken(sentiment = sentiment)
            PowerLinkToken(hasPowerLink = hasPowerLink)
        }
    }
}

//필터 버튼이 보이는 부분
@Composable
fun FilterButtonView(
    onFilterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = onFilterClicked,
        ) {
            Icon(imageVector = Icons.Default.List, contentDescription = null)
        }
    }
}

//각 블로그 포스트들의 주요 키워드들을 보여주는 컴포저블
@Composable
fun KeywordsView(
    keywords: List<String>,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.hashtag_view_title),
        style = Typography.h3
    )
    Spacer(modifier = Modifier.height(8.dp))

    if(keywords.isNotEmpty()) {
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            crossAxisAlignment = FlowCrossAxisAlignment.Center
        ) {
            keywords.forEach { keyword ->
                HashtagToken(keyword = keyword.deleteBoldTag())
            }
        }
    }
}

//단어 토큰
@Composable
fun WordToken(
    word: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color.Unspecified
) {
    Text (
        text = word,
        color = textColor,
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(100)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(100)
            )
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        maxLines = 1
    )
}

//키워드 토큰
@Composable
fun HashtagToken(
    keyword: String,
    modifier: Modifier = Modifier
) {
    WordToken(
        word = keyword,
        borderColor = Color.Green,
        modifier = modifier
    )
}

//긍/부정 토큰
@Composable
fun SentimentToken(
    sentiment: Sentiment,
    modifier: Modifier = Modifier
) {
    val text = when(sentiment) {
        Sentiment.POSITIVE -> stringResource(id = R.string.positive)
        Sentiment.NEUTRAL -> stringResource(id = R.string.neutrality)
        Sentiment.NEGATIVE -> stringResource(id = R.string.negative)
    }

    WordToken(
        word = stringResource(id = R.string.sentiment_token, text),
        textColor = sentiment.color,
        borderColor = sentiment.color,
        modifier = modifier
    )
}

//파워링크 토큰
@Composable
fun PowerLinkToken(
    hasPowerLink: Boolean,
    modifier: Modifier = Modifier
) {
    val onSurColor = MaterialTheme.colors.onSurface
    val surColor = MaterialTheme.colors.surface

    val color = if(hasPowerLink) {
        onSurColor
    } else {
        val r = (onSurColor.red+surColor.red*4f)/5f
        val g = (onSurColor.green+surColor.green*4f)/5f
        val b = (onSurColor.blue+surColor.blue*4f)/5f

        Log.d("RGB", "r = $r, g = $g, b = $b")
        Color(r, g, b)
    }

    WordToken(
        word = stringResource(id = R.string.power_link_token, if(hasPowerLink) "있음" else "없음"),
        textColor = color,
        borderColor = color,
        modifier = modifier
    )
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
fun SearchResultItemPreview() {
    GreenJuiceTheme {
        SearchResultItem(
            juiceItem = JuiceItem(
                title = "Title",
                description = "description",
                juiceColor = JuiceColor.RED,
                sentiment = Sentiment.POSITIVE,
                hasPowerLink = false,
                hashtags = listOf("apple", "banana", "grape", "kiwi", "pineapple", "mint", "dragon fruit")
            )
        )
    }
}

fun String.deleteBoldTag(): String {
    return this.replace("<b>", "").replace("</b>", "")
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
    LoadingScreen()
}