package com.starbow.greenjuice.serializable

import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
class SignInResult(
    private val login: String,
    private val accessToken: String,
) {
    fun getLogin() = login.toBoolean()
    fun getAccessToken() = accessToken
}