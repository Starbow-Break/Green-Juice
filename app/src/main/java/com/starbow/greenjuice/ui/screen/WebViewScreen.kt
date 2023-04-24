package com.starbow.greenjuice.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.*
import com.starbow.greenjuice.ui.theme.GreenJuiceTheme

const val TAG: String = "WebView"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier
) {
    val webViewState = rememberWebViewState(url = url)
    val webViewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()
    val webViewNavigator = rememberWebViewNavigator()

    Log.d(TAG, "open web : " + url)

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        client = webViewClient,
        chromeClient = webChromeClient,
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = false
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun WebViewScreenPreview() {
    GreenJuiceTheme {
        WebViewScreen("")
    }
}