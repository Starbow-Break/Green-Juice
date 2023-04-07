package com.starbow.greenjuice.data

import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.model.toJuiceItem
import com.starbow.greenjuice.network.BlogRequest
import com.starbow.greenjuice.network.GreenJuiceApiService

interface GreenJuiceRepository {
    suspend fun search(query: String, start: Int, display: Int): List<JuiceItem>
}

class NetworkGreenJuiceRepository(
    private val greenJuiceApiService: GreenJuiceApiService
) : GreenJuiceRepository {
    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        /* 서버랑 연결이 전부 완료되면 주석 지우기*/
        return greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }

        //테스트 용 코드
        /*delay(5000L)
        return SampleDataSource.search(query, start, display)*/
    }
}