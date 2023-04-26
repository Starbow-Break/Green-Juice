package com.starbow.greenjuice.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
            modifier = modifier.padding(horizontal = 8.dp).align(Alignment.Center)
        ) {
            var id by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var passwordCheck by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text(stringResource(id = R.string.id)) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password)) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = passwordCheck,
                onValueChange = { passwordCheck = it },
                label = { Text(stringResource(id = R.string.password_check)) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            Button(
                onClick = {
                    onSignUpClick(id, password)
                },
                enabled = validId(id) and validPassword(password) and (password == passwordCheck),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text(stringResource(id = R.string.sign_up))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    GreenJuiceTheme {
        SignUpScreen(
            onSignUpClick = {a, b -> }
        )
    }
}

fun validId(id: String): Boolean {
    val regexId = "[a-zA-Z\\d]{6,18}".toRegex()
    return regexId.matches(id)
}

fun validPassword(password: String): Boolean {
    val regexPw = "[a-zA-Z\\d]{6,18}".toRegex()
    return regexPw.matches(password)
}