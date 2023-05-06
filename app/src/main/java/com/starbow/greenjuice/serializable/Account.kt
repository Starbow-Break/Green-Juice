package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class Account(
    private val user_id: String,
    private val password: String
) {
    fun getId() = user_id
    fun getPassword() = password
}