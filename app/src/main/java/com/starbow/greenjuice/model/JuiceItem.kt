package com.starbow.greenjuice.model

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment

data class JuiceItem(
    val id: Int,
    val postUrl: String,
    val title: String,
    val description: String,
    val juiceColor: JuiceColor?,
    val sentiment: Sentiment?,
    val hashtags: List<String>
)