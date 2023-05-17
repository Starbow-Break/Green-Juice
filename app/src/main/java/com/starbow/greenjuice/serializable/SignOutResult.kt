package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class SignOutResult(
    private val logout: Boolean
) {
    fun getLogout() = logout
}