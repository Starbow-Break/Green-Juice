package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R

@Composable
fun SignInScreen(
    onSignInClick: (String, String) -> Unit,
    onClickSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 32.dp)
                .align(Alignment.Center)
        ) {
            var id by rememberSaveable { mutableStateOf("") } //텍스트 필드에 입력된 아이디
            var password by rememberSaveable { mutableStateOf("") } //텍스트 필드에 입력된 비밀번호

            TextField(
                value = id,
                onValueChange = {
                    val regex = "\\w*".toRegex()
                    if(regex.matches(it)) id = it
                },
                label = { Text(stringResource(id = R.string.id)) },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            TextField(
                value = password,
                onValueChange = {
                    val regex = "[A-Za-z\\d]*".toRegex()
                    if(regex.matches(it)) password = it
                },
                label = { Text(stringResource(id = R.string.password)) },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = { onSignInClick(id, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(stringResource(id = R.string.sign_in))
            }
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp)
        ) {
            Text(
                text = "계정이 없으신가요?",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(8.dp)
            )
            Divider(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
            )
            TextButton(onClick = onClickSignUp) {
                Text(text = stringResource(id = R.string.sign_up))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    GreenJuiceTheme {
        SignInScreen(
            onSignInClick = {_, _ -> },
            onClickSignUp = {}
        )
    }
}