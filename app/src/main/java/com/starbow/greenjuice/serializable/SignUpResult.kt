package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class SignUpResult(
    private val message: String
) {
    fun getMessage() = message
}