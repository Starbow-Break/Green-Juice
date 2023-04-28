package com.starbow.greenjuice.data

import android.accounts.Account
import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.model.toJuiceItem
import com.starbow.greenjuice.network.BlogRequest
import com.starbow.greenjuice.network.GreenJuiceApiService

interface GreenJuiceRepository {
    suspend fun search(query: String, start: Int, display: Int): List<JuiceItem>
    suspend fun signUp(id: String, password: String)
    suspend fun authentication(id: String, password: String)
}
class NetworkGreenJuiceRepository(
    private val greenJuiceApiService: GreenJuiceApiService
) : GreenJuiceRepository {
    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        return greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }
    }

    override suspend fun signUp(id: String, password: String) {
        greenJuiceApiService.signUp(Account(id, password))
    }

    override suspend fun authentication(id: String, password: String) {
        greenJuiceApiService.authentication(Account(id, password))
    }
}