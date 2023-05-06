package com.starbow.greenjuice.serializable

import kotlinx.serialization.Serializable

@Serializable
class BlogRequest(
    private val query: String,
    private val start: Int,
    private val display: Int,
) {
    fun getQuery() = query
    fun getStart() = start
    fun getDisplay() = display
}