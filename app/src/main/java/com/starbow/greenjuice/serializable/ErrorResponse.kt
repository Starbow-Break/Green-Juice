package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse(
    private val ok: Boolean,
    private val message: String
) {
    fun getOk() = ok
    fun getMessage() = message
}