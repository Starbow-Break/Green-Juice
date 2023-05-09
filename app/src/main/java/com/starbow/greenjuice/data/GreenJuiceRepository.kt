package com.starbow.greenjuice.data

import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.network.GreenJuiceApiService
import com.starbow.greenjuice.serializable.*

interface GreenJuiceRepository {
    suspend fun search(query: String, start: Int, display: Int): List<JuiceItem>
    suspend fun signIn(id: String, password: String): SignInResult
    suspend fun signOut(token: String): Boolean
    suspend fun signUp(id: String, password: String): Boolean
    suspend fun isIdExist(id: String): Boolean
    suspend fun getFavorites(token: String): List<JuiceItem>
    suspend fun addFavorites(token: String, postId: Int)
    suspend fun deleteFavorites(token: String, postId: Int)
}
class NetworkGreenJuiceRepository(
    private val greenJuiceApiService: GreenJuiceApiService,
) : GreenJuiceRepository {
    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        return greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }
    }

    override suspend fun signIn(id: String, password: String): SignInResult {
        return greenJuiceApiService.signIn(Account(id, password))
    }

    override suspend fun signOut(token: String): Boolean {
        return greenJuiceApiService.signOut(token).resultToBoolean()
    }

    override suspend fun signUp(id: String, password: String): Boolean {
        val message = greenJuiceApiService.signUp(Account(id, password))
        return message.getMessage() == "회원가입 완료"
    }

    override suspend fun isIdExist(id: String): Boolean {
        return greenJuiceApiService.isIdExist(id).toBoolean()
    }

    override suspend fun getFavorites(token: String): List<JuiceItem> {
        return greenJuiceApiService.getFavorites(token).map { it.toJuiceItem() }
    }

    override suspend fun addFavorites(token: String, postId: Int) {
        greenJuiceApiService.addFavorites(token, postId)
    }

    override suspend fun deleteFavorites(token: String, postId: Int) {
        greenJuiceApiService.deleteFavorites(token, postId)
    }
}