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
    suspend fun getFavorites(accountId: String): List<Int>
    suspend fun addFavorites(accountId: String, postId: Int)
    suspend fun deleteFavorites(accountId: String, postId: Int)
}
class NetworkGreenJuiceRepository(
    private val greenJuiceApiService: GreenJuiceApiService
) : GreenJuiceRepository {
    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        return SampleDataSource.dataList/*greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }*/
    }

    override suspend fun signUp(id: String, password: String) {
        greenJuiceApiService.signUp(Account(id, password))
    }

    override suspend fun authentication(id: String, password: String) {
        greenJuiceApiService.authentication(Account(id, password))
    }

    override suspend fun getFavorites(accountId: String): List<Int> {
        /*greenJuiceApiService.getFavorites(accountId)*/
        return SampleDataSource.favoritesMap[accountId] ?: listOf()
    }

    override suspend fun addFavorites(accountId: String, postId: Int) {
        /*greenJuiceApiService.addFavorites(accountId, postId)*/
        SampleDataSource.favoritesMap[accountId]?.add(postId)
            ?: SampleDataSource.favoritesMap.put(accountId, mutableListOf(postId))
    }

    override suspend fun deleteFavorites(accountId: String, postId: Int) {
        /*greenJuiceApiService.deleteFavorites(accountId, postId)*/
        SampleDataSource.favoritesMap[accountId]?.remove(postId)
    }
}