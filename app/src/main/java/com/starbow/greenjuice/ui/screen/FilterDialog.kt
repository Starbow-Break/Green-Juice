package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.Typography

//필터 버튼을 클릭하면 나오는 AlertDialog
@Composable
fun FilterDialog(
    adStateOptions: List<String>,
    selectedJuiceOptionIndex: Int,
    onJuiceChanged: (Int) -> Unit,
    sentimentOptions: List<String>,
    selectedSentimentOptionIndex: Int,
    onSentimentChanged: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(id = R.string.filter_dialog_title),
                style = Typography.h2
            )
        },
        text = {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                RadioGroup(
                    groupTitle = stringResource(id = R.string.juice_title),
                    optionList = adStateOptions,
                    selectedIndex = selectedJuiceOptionIndex,
                    onChangeSelected = onJuiceChanged
                )

                RadioGroup(
                    groupTitle = stringResource(id = R.string.sentiment_title),
                    optionList = sentimentOptions,
                    selectedIndex = selectedSentimentOptionIndex,
                    onChangeSelected = onSentimentChanged
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(text = stringResource(id = R.string.confirm_button_text))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text(text = stringResource(id = R.string.dismiss_button_text))
            }
        }
    )
}