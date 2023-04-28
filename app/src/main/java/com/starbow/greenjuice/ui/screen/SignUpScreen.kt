package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 32.dp)
                .align(Alignment.Center)
        ) {
            var id by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var passwordCheck by rememberSaveable { mutableStateOf("") }

            TextFieldWithDescription(
                value = id,
                description = "6~18자리/영문 대소문자, 숫자, 특수문자'_' 조합",
                onValueChange = {
                    val regex = "\\w*".toRegex()
                    if(regex.matches(it)) id = it
                },
                label = { Text(stringResource(id = R.string.id)) },
                maxLines = 1,
                isError = id.isNotEmpty() and !validId(id),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            TextFieldWithDescription(
                value = password,
                description = "6~18자리/영문 대소문자, 숫자 조합",
                onValueChange = {
                    val regex = "[A-Za-z\\d]*".toRegex()
                    if(regex.matches(it)) password = it
                },
                label = { Text(stringResource(id = R.string.password)) },
                maxLines = 1,
                isError = password.isNotEmpty() and !validPassword(password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            TextField(
                value = passwordCheck,
                onValueChange = {
                    val regex = "[A-Za-z\\d]*".toRegex()
                    if(regex.matches(it)) passwordCheck = it
                },
                label = { Text(stringResource(id = R.string.password_check)) },
                maxLines = 1,
                isError = passwordCheck.isNotEmpty() and (password != passwordCheck),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Button(
                onClick = {
                    onSignUpClick(id, password)
                },
                enabled = validId(id) and validPassword(password) and (password == passwordCheck),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(stringResource(id = R.string.sign_up))
            }
        }
    }
}

@Composable
fun TextFieldWithDescription(
    value: String,
    description: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    maxLines: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column (modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            maxLines = maxLines,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = description,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun TextFieldWithDescriptionPreview() {
    TextFieldWithDescription(
        value = "value",
        description = "description",
        onValueChange = {},
        label = { Text("label") },
        maxLines = 1,
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    GreenJuiceTheme {
        SignUpScreen(
            onSignUpClick = {_, _ -> }
        )
    }
}

fun validId(id: String): Boolean {
    val regexId = "\\w{6,18}".toRegex()
    return regexId.matches(id)
}

fun validPassword(password: String): Boolean {
    val regexPw = "[a-zA-Z\\d]{6,18}".toRegex()
    return regexPw.matches(password)
}