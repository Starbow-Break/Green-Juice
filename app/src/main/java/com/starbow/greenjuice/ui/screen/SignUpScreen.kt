package com.starbow.greenjuice.ui.screen

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

@Composable
fun SignUpScreen(
    onIsValidClick: (String) -> Boolean,
    onSignUpClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
        ) {
            var id by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var passwordCheck by rememberSaveable { mutableStateOf("") }
            var isValid by rememberSaveable { mutableStateOf(false) }

            IDTextField(
                textFieldEnabled = !isValid,
                value = id,
                description = "6~18자리/영문 대소문자, 숫자, 특수문자'_' 조합",
                onValueChange = {
                    val regex = "\\w*".toRegex()
                    if(regex.matches(it)) id = it
                },
                label = { Text(stringResource(id = R.string.id)) },
                buttonEnabled = validId(id) and !isValid,
                buttonLabel = { Text(text = "확인") },
                onButtonClick = {
                    if(onIsValidClick(id)) {
                        isValid = true
                        Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(context, "중복된 아이디입니다.", Toast.LENGTH_LONG).show()
                    }
                },
                maxLines = 1,
                isError = id.isNotEmpty() and !validId(id),
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
            PasswordTextField(
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
            PasswordTextField(
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
                enabled = isValid and validPassword(password) and (password == passwordCheck),
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
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    maxLines: Int,
    modifier: Modifier = Modifier,
    description: String? = null,
    isError: Boolean = false
) {
    Column (modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            maxLines = maxLines,
            isError = isError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if(description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun IDTextField(
    textFieldEnabled: Boolean,
    value: String,
    description: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    maxLines: Int,
    buttonEnabled: Boolean,
    buttonLabel: @Composable () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column (modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                enabled = textFieldEnabled,
                value = value,
                onValueChange = onValueChange,
                label = label,
                maxLines = maxLines,
                isError = isError,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(
                enabled = buttonEnabled,
                onClick = onButtonClick,
                modifier = modifier
                    .width(80.dp)
                    .height(TextFieldDefaults.MinHeight),
            ) {
                buttonLabel()
            }
        }
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
fun PasswordTextFieldPreview() {
    GreenJuiceTheme {
        PasswordTextField(
            value = "value",
            description = "description",
            onValueChange = {},
            label = { Text("label") },
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IDTextFieldPreview() {
    GreenJuiceTheme {
        IDTextField(
            value = "value",
            description = "description",
            onValueChange = {},
            label = { Text("label") },
            maxLines = 1,
            buttonEnabled = true,
            buttonLabel = {},
            onButtonClick = {},
            textFieldEnabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    GreenJuiceTheme {
        SignUpScreen(
            onSignUpClick = {_, _ -> },
            onIsValidClick = {false}
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