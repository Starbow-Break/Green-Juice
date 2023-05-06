package com.starbow.greenjuice.data

import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.network.GreenJuiceApiService
import com.starbow.greenjuice.serializable.Account
import com.starbow.greenjuice.serializable.BlogRequest
import com.starbow.greenjuice.serializable.toBoolean
import com.starbow.greenjuice.serializable.toJuiceItem

interface GreenJuiceRepository {
    suspend fun search(query: String, start: Int, display: Int): List<JuiceItem>
    suspend fun signIn(id: String, password: String): Boolean
    suspend fun signOut(): Boolean
    suspend fun signUp(id: String, password: String): Boolean
    suspend fun isIdExist(id: String): Boolean
    suspend fun getFavorites(): List<JuiceItem>
    suspend fun addFavorites(postId: Int)
    suspend fun deleteFavorites(postId: Int)
}
class NetworkGreenJuiceRepository(
    private val greenJuiceApiService: GreenJuiceApiService
) : GreenJuiceRepository {
    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        return greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }
    }

    override suspend fun signIn(id: String, password: String): Boolean {
        return greenJuiceApiService.signIn(Account(id, password)).toBoolean()
    }

    override suspend fun signOut(): Boolean {
        return greenJuiceApiService.signOut().toBoolean()
    }

    override suspend fun signUp(id: String, password: String): Boolean {
        val message = greenJuiceApiService.signUp(Account(id, password))
        return message.getMessage() == "회원가입 완료"
    }

    override suspend fun isIdExist(id: String): Boolean {
        return greenJuiceApiService.isIdExist(id).toBoolean()
    }

    override suspend fun getFavorites(): List<JuiceItem> {
        return greenJuiceApiService.getFavorites().map { it.toJuiceItem() }
    }

    override suspend fun addFavorites(postId: Int) {
        greenJuiceApiService.addFavorites(postId)
    }

    override suspend fun deleteFavorites(postId: Int) {
        greenJuiceApiService.deleteFavorites(postId)
    }
}