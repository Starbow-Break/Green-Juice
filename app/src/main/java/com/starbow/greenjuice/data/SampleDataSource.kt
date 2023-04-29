package com.starbow.greenjuice.data

import com.starbow.greenjuice.enum.JuiceColor
import com.starbow.greenjuice.enum.Sentiment
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.network.Account

object SampleDataSource {
    //초기 데이터
    val dataList = mutableListOf(
        JuiceItem(
            postUrl = "https://blog.naver.com/play_eat_fun/223085673944",
            title = "Title1",
            description = "description1",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.POSITIVE,
            favorites = false,
            hasPowerLink = true,
            hashtags = listOf("keyword1_1", "keyword1_2", "keyword1_3")
        ),
        JuiceItem(
            postUrl = "https://blog.naver.com/yugin0303/223085668684",
            title = "Title2",
            description = "description2",
            juiceColor = JuiceColor.RED,
            sentiment = Sentiment.POSITIVE,
            favorites = false,
            hasPowerLink = false,
            hashtags = listOf("keyword2_1", "keyword2_2")
        ),
        JuiceItem(
            postUrl = "https://blog.naver.com/sayu0125/223085660825",
            title = "Title3",
            description = "description3",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEUTRAL,
            favorites = true,
            hasPowerLink = true,
            hashtags = listOf("keyword3_1", "keyword3_2", "keyword3_3", "keyword3_4", "keyword3_5")
        ),
        JuiceItem(
            postUrl = "https://blog.naver.com/moy99/223085659988",
            title = "Title4",
            description = "description4",
            juiceColor = JuiceColor.ORANGE,
            sentiment = Sentiment.POSITIVE,
            favorites = true,
            hasPowerLink = true,
            hashtags = listOf("keyword4_1")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title5",
            description = "description5",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            favorites = false,
            hasPowerLink = false,
            hashtags = listOf("keyword5_1", "keyword5_2", "keyword5_3", "keyword5_4")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title6",
            description = "description6",
            juiceColor = JuiceColor.ORANGE,
            sentiment = Sentiment.POSITIVE,
            favorites = true,
            hasPowerLink = false,
            hashtags = listOf("keyword6_1", "keyword6_2", "keyword6_3")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title7",
            description = "description7",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            favorites = true,
            hasPowerLink = true,
            hashtags = listOf("keyword7_1", "keyword7_2")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title8",
            description = "description8",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEUTRAL,
            favorites = true,
            hasPowerLink = false,
            hashtags = listOf("keyword8_1", "keyword8_2")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title9",
            description = "description9",
            juiceColor = JuiceColor.RED,
            sentiment = Sentiment.POSITIVE,
            favorites = false,
            hasPowerLink = true,
            hashtags = listOf("keyword9_1")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title10",
            description = "description10",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.NEGATIVE,
            favorites = true,
            hasPowerLink = false,
            hashtags = listOf("keyword10_1", "keyword10_2", "keyword10_3", "keyword10_4",  "keyword10_5", "keyword10_6")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title11",
            description = "description11",
            juiceColor = JuiceColor.RED,
            sentiment = Sentiment.NEUTRAL,
            favorites = true,
            hasPowerLink = true,
            hashtags = listOf("keyword11_1", "keyword11_2", "keyword11_3")
        ),
        JuiceItem(
            postUrl = "",
            title = "Title12",
            description = "description12",
            juiceColor = JuiceColor.GREEN,
            sentiment = Sentiment.POSITIVE,
            favorites = false,
            hasPowerLink = true,
            hashtags = listOf("keyword12_1")
        )
    )

    //새로운 단어로 검색
    fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        if(start >= dataList.size) return listOf()

        val end = if(start+display-1 >= dataList.size) dataList.size else start+display-1
        return dataList.subList(fromIndex = start-1, toIndex = end)
    }

    private val accountList = mutableListOf(
        Account("aaaa", "aaaa"),
        Account("bbbb", "bbbb"),
        Account("cccc", "cccc")
    )

    fun hasId(id: String): Boolean {
        for(account in accountList) {
            if(account.getId() == id) return true
        }

        return false
    }

    fun addAccount(id: String, password: String) {
        accountList.add(Account(id, password))
    }

    fun authentication(id: String, password: String): Boolean {
        for(account in accountList)
            if((account.getId() == id) and (account.getPassword() == password)) return true

        return false
    }
}