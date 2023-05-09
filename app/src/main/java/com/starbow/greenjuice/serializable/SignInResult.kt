package com.starbow.greenjuice.serializable

import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
class SignInResult(
    private val login: String,
    private val access_token: String,
    private val refresh_token: String
) {
    fun getLogin() = login
    fun getAccessToken() = login
    fun getRefreshToken() = login
}

fun SignInResult.resultToBoolean(): Boolean {
    Log.d("LoginCheck", "result = ${getLogin()}")
    return this.getLogin().toBoolean()
}