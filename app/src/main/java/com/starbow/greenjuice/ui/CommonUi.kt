package com.starbow.greenjuice.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.ui.screen.deleteBoldTag
import com.starbow.greenjuice.ui.theme.*

//앱 타이틀
@Composable
fun AppTitle(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.app_name),
        style = Typography.h1,
        textAlign = TextAlign.Center,
        color = Green500,
        modifier = modifier
    )
}

//검색 바
@Composable
fun SearchBar(
    query: String,
    onChangeQuery: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    //SearchBar 에 사용할 leadingIcon
    val leadingIconView = @Composable {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null
        )
    }

    //SearchBar 에 사용할 trailingIcon
    val trailingIconView = @Composable {
        IconButton(
            onClick = onClearQuery
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }
    }

    OutlinedTextField(
        value = query,
        onValueChange = onChangeQuery,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        shape = RoundedCornerShape(100),
        leadingIcon =  leadingIconView,
        trailingIcon = if(query.isNotBlank()) trailingIconView else null,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Green
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}



//라디오 버튼의 집합체
@Composable
fun RadioGroup(
    groupTitle: String,
    optionList: List<String>,
    selectedIndex: Int,
    onChangeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = groupTitle,
            style = Typography.h3
        )
        Spacer(modifier = Modifier.height(8.dp))
        optionList.forEachIndexed { idx, text ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (idx == selectedIndex),
                    onClick = { onChangeSelected(idx) }
                )
                Text(text = text)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    GreenJuiceTheme {
        SearchBar(query = "test", onChangeQuery = {}, onSearch = {}, onClearQuery = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupPreview() {
    GreenJuiceTheme {
        Surface(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
        ) {
            RadioGroup(
                groupTitle = "Title",
                optionList = listOf("item1", "item2", "item3"),
                selectedIndex = 1,
                onChangeSelected = {}
            )
        }
    }
}

//검색 결과로 나오는 블로그 글들의 분석 결과를 보여주기 위한 리스트 아이템
@Composable
fun SearchResultItem(
    juiceItem: JuiceItem,
    showFavorites: Boolean,
    onCardClick: (String) -> Unit,
    addFavorites: (Int) -> Unit,
    deleteFavorites: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCardClick(juiceItem.postUrl)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                JuiceResultView(juiceColor = juiceItem.juiceColor)
                Spacer(modifier = Modifier.width(8.dp))
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = juiceItem.title.deleteBoldTag(), style = Typography.h3)

                        if(showFavorites) {
                            IconButton(
                                onClick = {
                                    if(isFavorite) deleteFavorites(juiceItem.id)
                                    else addFavorites(juiceItem.id)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if(isFavorite) R.drawable.star else R.drawable.star_outline
                                    ),
                                    tint = YellowA400,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = juiceItem.description.deleteBoldTag())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SentimentView(sentiment = juiceItem.sentiment)
            Spacer(modifier = Modifier.height(16.dp))
            HashtagsView(juiceItem.hashtags)
        }
    }
}

@Composable
fun JuiceResultView(
    juiceColor: JuiceColor?,
    modifier: Modifier = Modifier
) {
    JuiceResultViewItem(
        juiceColor = juiceColor,
        text = if(juiceColor != null) stringResource(id = R.string.juice_with_color, stringResource(id = juiceColor.stringRes))
        else stringResource(id = R.string.unknown),
        modifier = modifier
    )
}

@Composable
fun JuiceResultViewItem(
    juiceColor: JuiceColor?,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.defaultMinSize(64.dp)
    ) {
        if (juiceColor != null) {
            Image(
                painter = painterResource(id = juiceColor.imgRes),
                contentDescription = text,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(48.dp)
            )
            Text(text = text, color = juiceColor.color, textAlign = TextAlign.Center)
        } else {
            Image(
                painter = painterResource(id = R.drawable.unknown),
                contentDescription = text,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(48.dp)
            )
            Text(text = text, color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center)
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
fun SentimentView(
    sentiment: Sentiment?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.sentiment_title),
            style = Typography.h3
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            SentimentToken(
                sentiment = sentiment,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

//각 블로그 포스트들의 주요 키워드들을 보여주는 컴포저블
@Composable
fun HashtagsView(
    hashtags: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.hashtag_view_title),
            style = Typography.h3
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (hashtags.isNotEmpty()) {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                mainAxisAlignment = FlowMainAxisAlignment.Center,
                crossAxisAlignment = FlowCrossAxisAlignment.Center
            ) {
                hashtags.forEach { keyword ->
                    HashtagToken(keyword = keyword.deleteBoldTag())
                }
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
    sentiment: Sentiment?,
    modifier: Modifier = Modifier
) {
    if(sentiment != null) {
        WordToken(
            word = stringResource(
                id = R.string.sentiment_token,
                stringResource(id = sentiment.stringRes)
            ),
            textColor = sentiment.color,
            borderColor = sentiment.color,
            modifier = modifier
        )
    }
    else {
        WordToken(
            word = stringResource(id = R.string.unknown),
            textColor = MaterialTheme.colors.onSurface,
            borderColor = MaterialTheme.colors.onSurface,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, heightDp = 100)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, heightDp = 100)
@Composable
fun UnknownJuicePreview() {
    GreenJuiceTheme {
        Surface {
            JuiceResultView(juiceColor = null)
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true,)
@Composable
fun UnknownTokenPreview() {
    GreenJuiceTheme {
        Surface {
            SentimentToken(sentiment = null)
        }
    }
}

@Preview
@Composable
fun SearchResultItemPreview() {
    GreenJuiceTheme {
        SearchResultItem(
            onCardClick = {},
            showFavorites = true,
            juiceItem = JuiceItem(
                id = 1,
                postUrl = "",
                title = "Title",
                description = "description",
                juiceColor = JuiceColor.RED,
                sentiment = Sentiment.POSITIVE,
                hashtags = listOf("apple", "banana", "grape", "kiwi", "pineapple", "mint", "dragon fruit"),
            ),
            addFavorites = {},
            deleteFavorites = {}
        )
    }
}