package com.starbow.greenjuice.model

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment

data class JuiceItem(
    val title: String,
    val description: String,
    val juiceColor: JuiceColor?,
    val sentiment: Sentiment?,
    val hasPowerLink: Boolean,
    val hashtags: List<String>
)