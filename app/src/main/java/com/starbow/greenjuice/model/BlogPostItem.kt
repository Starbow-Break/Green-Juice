package com.starbow.greenjuice.model

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

//서버로부터 받은 데이터를 여기에 객체화해서 저장
@Serializable
data class BlogPostItem(
    val title: String,
    @SerialName("post_description")
    val description: String,
    val juice: String,
    val sentiment: String,
    @SerialName("powerlink")
    val powerLink: Int,
    val hashtags: String,
    val id: Int,
    val link: String,
    val search_query: String,
    @SerialName("posttime")
    val postTime: String
)

fun BlogPostItem.toJuiceItem(): JuiceItem {
    val juiceColor = JuiceColor.valueOf(if(juice.contains("yellow")) "ORANGE" else juice.uppercase())
    val sentiment = Sentiment.valueOf(sentiment.uppercase())
    val hashtags = if(hashtags.isBlank() or hashtags.isEmpty()) emptyList() else hashtags.split(" ")

    return JuiceItem(
        title = title,
        description = description,
        juiceColor = juiceColor,
        sentiment = sentiment,
        hasPowerLink = powerLink == 1,
        hashtags = hashtags
    )
}