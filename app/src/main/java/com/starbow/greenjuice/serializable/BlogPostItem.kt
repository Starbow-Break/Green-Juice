package com.starbow.greenjuice.serializable

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

//서버로부터 받은 데이터를 여기에 객체화해서 저장
@Serializable
data class BlogPostItem(
    val id: Int,
    val title: String,
    val link: String,
    val post_description: String,
    val hashtags: String?,
    val juice: String?,
    val sentiment: String,
    val search_query: String,
    val powerlink: Int,
    val created_at: String,
    val updated_at: String
)

fun BlogPostItem.toJuiceItem(): JuiceItem {
    val juiceColor = when(juice) {
        "green", "orange", "red" -> JuiceColor.valueOf(juice.uppercase())
        else -> null
    }
    val sentiment = when(sentiment) {
        "positive", "neutral", "negative" -> Sentiment.valueOf(sentiment.uppercase())
        else -> null
    }

    val hashtags = if(hashtags != null) {
        if (hashtags.isBlank() or hashtags.isEmpty()) emptyList()
        else hashtags.split("#").filter { hashtag ->
            hashtag.isNotBlank() and hashtag.isNotEmpty()
        }.map { hashtag -> "#$hashtag" }
    } else {
        emptyList()
    }

    return JuiceItem(
        id = id,
        postUrl = link,
        title = title,
        description = post_description,
        juiceColor = juiceColor,
        sentiment = sentiment,
        hasPowerLink = powerlink == 1,
        hashtags = hashtags
    )
}
