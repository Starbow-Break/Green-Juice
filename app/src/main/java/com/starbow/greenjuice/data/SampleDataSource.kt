package com.starbow.greenjuice.data

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.serializable.Account

object SampleDataSource {
    //초기 데이터
    val dataList = mutableListOf(
        JuiceItem(
            id = 1,
            postUrl = "https://blog.naver.com/play_eat_fun/223085673944",
            title = "Title1",
            description = "description1",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.POSITIVE,
            hashtags = listOf("keyword1_1", "keyword1_2", "keyword1_3")
        ),
        JuiceItem(
            id = 2,
            postUrl = "https://blog.naver.com/yugin0303/223085668684",
            title = "Title2",
            description = "description2",
            juiceColor = JuiceColor.RED,
            sentiment = Sentiment.POSITIVE,
            hashtags = listOf("keyword2_1", "keyword2_2")
        ),
        JuiceItem(
            id = 3,
            postUrl = "https://blog.naver.com/sayu0125/223085660825",
            title = "Title3",
            description = "description3",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEUTRAL,
            hashtags = listOf("keyword3_1", "keyword3_2", "keyword3_3", "keyword3_4", "keyword3_5")
        ),
        JuiceItem(
            id = 4,
            postUrl = "https://blog.naver.com/moy99/223085659988",
            title = "Title4",
            description = "description4",
            juiceColor = JuiceColor.ORANGE,
            sentiment = Sentiment.POSITIVE,
            hashtags = listOf("keyword4_1")
        ),
        JuiceItem(
            id = 5,
            postUrl = "",
            title = "Title5",
            description = "description5",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            hashtags = listOf("keyword5_1", "keyword5_2", "keyword5_3", "keyword5_4")
        ),
        JuiceItem(
            id = 6,
            postUrl = "",
            title = "Title6",
            description = "description6",
            juiceColor = JuiceColor.ORANGE,
            sentiment = Sentiment.POSITIVE,
            hashtags = listOf("keyword6_1", "keyword6_2", "keyword6_3")
        ),
        JuiceItem(
            id = 7,
            postUrl = "",
            title = "Title7",
            description = "description7",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            hashtags = listOf("keyword7_1", "keyword7_2")
        ),
        JuiceItem(
            id = 8,
            postUrl = "",
            title = "Title8",
            description = "description8",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEUTRAL,
            hashtags = listOf("keyword8_1", "keyword8_2")
        ),
        JuiceItem(
            id = 9,
            postUrl = "",
            title = "Title9",
            description = "description9",
            juiceColor = JuiceColor.RED,
            sentiment = Sentiment.POSITIVE,
            hashtags = listOf("keyword9_1")
        ),
        JuiceItem(
            id = 10,
            postUrl = "",
            title = "Title10",
            description = "description10",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            hashtags = listOf("keyword10_1", "keyword10_2", "keyword10_3", "keyword10_4",  "keyword10_5", "keyword10_6")
        )
    )

    private val accountList = mutableListOf(
        Account("aaaaaa", "aaaaaa"),
        Account("bbbbbb", "bbbbbb"),
        Account("cccccc", "cccccc")
    )
}