package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class ExistId(
    private val exist: String
) {
    fun getResult() = exist
}

fun ExistId.toBoolean(): Boolean {
    return this.getResult().toBoolean()
}