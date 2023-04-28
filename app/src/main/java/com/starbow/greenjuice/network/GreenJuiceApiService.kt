package com.starbow.greenjuice.network

import android.accounts.Account
import com.starbow.greenjuice.model.BlogPostItem
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface GreenJuiceApiService {
    @POST("/api/posts") // <- 서비스 URL 나머지 부분 넣기
    suspend fun search(@Body blogRequest: BlogRequest): List<BlogPostItem>

    @PUT
    suspend fun signUp(@Body account: Account)

    @GET
    suspend fun authentication(@Body account: Account): Boolean
}

@Serializable
class BlogRequest(
    private val query: String,
    private val start: Int,
    private val display: Int,
)

@Serializable
class Account(private val id: String, private val password: String) {
    fun getId() = id

    fun getPassword() = password
}