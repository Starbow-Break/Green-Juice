package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.data.SampleDataSource
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

@Composable
fun FavoritesScreen(
    favoritesList: List<JuiceItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp)
    ) {
        /* 현재 서버가 구현되지 않아 UI를 테스트 하는 코드로 작성됨 */
        items(favoritesList) { favorites ->
            SearchResultItem(
                juiceItem = favorites,
                onCardClick = onItemClick,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    GreenJuiceTheme {
        FavoritesScreen(
            favoritesList = SampleDataSource.dataList,
            onItemClick = {_ ->}
        )
    }
}