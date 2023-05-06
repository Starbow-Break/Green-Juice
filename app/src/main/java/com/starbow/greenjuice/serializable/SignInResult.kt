package com.starbow.greenjuice.serializable

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SignInResult(
    private val login: String
) {
    fun getResult() = login
}

fun SignInResult.toBoolean(): Boolean {
    Log.d("LoginCheck", "result = ${getResult()}")
    return this.getResult().toBoolean()
}