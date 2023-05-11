package com.starbow.greenjuice.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.starbow.greenjuice.R
import com.starbow.greenjuice.enum.EventToastMessage
import com.starbow.greenjuice.ui.AppViewModelProvider
import com.starbow.greenjuice.ui.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    onClickSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory),
    doSuccessSignIn: () -> Unit = {},
    navBackBlocked: (Int) -> Unit = {_ -> },
    navBackWakeup: (Int) -> Unit = {_ -> }
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isLoadingState = viewModel.isLoading.collectAsState()

    val allowLettersOnId = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM1234567890_"
    val allowLettersOnPassword = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM1234567890"

    var id by rememberSaveable { mutableStateOf("") } //텍스트 필드에 입력된 아이디
    var password by rememberSaveable { mutableStateOf("") } //텍스트 필드에 입력된 비밀번호

    viewModel.showToast.observe(lifecycleOwner, Observer {
        it.getContentIfNotHandled()?.let { toastMessage ->
            if(toastMessage == EventToastMessage.SIGN_IN) {
                doSuccessSignIn()
                Toast.makeText(context, context.getString(R.string.sign_in_success, id), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, toastMessage.messageRes, Toast.LENGTH_SHORT).show()
            }
        }
    })

    if(isLoadingState.value) navBackBlocked(2) else navBackWakeup(2)

    BackHandler(
        enabled = (isLoadingState.value)
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
                .padding(horizontal = 32.dp)
                .align(Alignment.Center)
        ) {
            TextField(
                value = id,
                onValueChange = { text ->
                    id = text.filter{ allowLettersOnId.contains(it) }
                },
                enabled = !isLoadingState.value,
                label = { Text(stringResource(id = R.string.id)) },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            TextField(
                value = password,
                onValueChange = { text ->
                    password = text.filter{ allowLettersOnPassword.contains(it) }
                },
                enabled = !isLoadingState.value,
                label = { Text(stringResource(id = R.string.password)) },
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = { viewModel.signIn(id, password) },
                enabled = !isLoadingState.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if(isLoadingState.value) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text(stringResource(id = R.string.sign_in))
            }
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
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
            onClickSignUp = {}
        )
    }
}