package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.starbow.greenjuice.data.SampleDataSource
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

@Composable
fun FavoritesScreen(
    favoritesList: List<JuiceItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(favoritesList) { favorites ->
            SearchResultItem(juiceItem = favorites)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    GreenJuiceTheme {
        FavoritesScreen(
            favoritesList = SampleDataSource.dataList
        )
    }
}