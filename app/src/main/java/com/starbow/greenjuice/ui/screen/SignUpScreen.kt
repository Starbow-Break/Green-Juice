package com.starbow.greenjuice.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.EventToastMessage
import com.starbow.greenjuice.ui.AppViewModelProvider
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import com.starbow.greenjuice.ui.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    doSuccessSignUp: () -> Unit = {},
    navBackBlocked: (Int) -> Unit = {_ -> },
    navBackWakeup: (Int) -> Unit = {_ -> }
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isValidState = viewModel.isValid.collectAsState()
    val isValidLoadingState = viewModel.isValidLoading.collectAsState()

    var id by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordCheck by rememberSaveable { mutableStateOf("") }

    viewModel.showToast.observe(lifecycleOwner, Observer {
        it.getContentIfNotHandled()?.let { toastMessage ->
            if(toastMessage == EventToastMessage.SIGN_UP) doSuccessSignUp()
            Toast.makeText(context, toastMessage.messageRes, Toast.LENGTH_SHORT).show()
        }
    })

    if(isValidState.value or isValidLoadingState.value) navBackBlocked(3) else navBackWakeup(3)

    BackHandler(
        enabled = isValidState.value or isValidLoadingState.value
    ) {
        viewModel.requestRefuse()
    }

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
            IDTextField(
                textFieldEnabled = !isValidState.value,
                value = id,
                description = "6~18자리/영문 대소문자, 숫자, 특수문자'_' 조합",
                onValueChange = {
                    val regex = "\\w*".toRegex()
                    if(regex.matches(it)) id = it
                },
                label = { Text(stringResource(id = R.string.id)) },
                buttonEnabled = validId(id) and !isValidState.value and !isValidLoadingState.value,
                buttonLabel = {
                    if(isValidLoadingState.value) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text(text = "확인")
                },
                onButtonClick = {
                    viewModel.isIdExist(id)
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
                    viewModel.signUp(id, password)
                },
                enabled = isValidState.value and validPassword(password) and (password == passwordCheck),
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
        SignUpScreen()
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