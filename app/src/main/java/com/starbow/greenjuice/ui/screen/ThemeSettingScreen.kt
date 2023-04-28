package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R

//설정 화면 (테마 설정)
@Composable
fun ThemeSettingScreen(
    themeOptions: List<String>,
    selectedThemeOptionIndex: Int,
    onSelectedChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        groupTitle = stringResource(id = R.string.theme),
        optionList = themeOptions,
        selectedIndex = selectedThemeOptionIndex,
        onChangeSelected = { onSelectedChanged(it) },
        modifier = modifier.padding(16.dp)
    )
}