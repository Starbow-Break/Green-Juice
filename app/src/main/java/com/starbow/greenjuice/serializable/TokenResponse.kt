package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class TokenResponse(
    private val ok: Boolean,
    private val accessToken: String,
    private val refreshToken : String
) {
    fun getOk() = ok
    fun getAccessToken() = accessToken
    fun getRefreshToken() = refreshToken
}