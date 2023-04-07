package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.Green800
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import com.starbow.greenjuice.ui.theme.Typography

//앱 타이틀
@Composable
fun AppTitle(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.app_name),
        style = Typography.h1,
        textAlign = TextAlign.Center,
        color = Green800,
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