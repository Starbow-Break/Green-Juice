package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

//메인 화면 (처음 앱을 실행하고 아직 검색 기능을 사용하지 않은 상태에서 띄우는 화면)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    query: String = "",
    isSignIn: Boolean = false,
    onChangeQuery: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onClearQuery: () -> Unit = {},
    onClickSignIn: () -> Unit = {},
    onClickSignUp: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val focusManager = LocalFocusManager.current

        Box (
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                AppTitle(modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar(
                    query = query,
                    onChangeQuery = onChangeQuery,
                    onSearch = onSearch,
                    onClearQuery = onClearQuery,
                )
            }

            if(!isSignIn) {
                SignInSignUpButton(
                    onClickSignIn = onClickSignIn,
                    onClickSignUp = onClickSignUp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp)
                )
            }
        }
    }
}

@Composable
fun SignInSignUpButton(
    onClickSignIn: () -> Unit,
    onClickSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextButton(
            onClick = onClickSignIn,
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                color = MaterialTheme.colors.primary
            )
        }
        Divider(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .width(2.dp)
                .height(16.dp)
        )
        TextButton(
            onClick = onClickSignUp,
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainWindowPreview() {
    GreenJuiceTheme {
        MainScreen()
    }
}





