package com.starbow.greenjuice.network

import com.starbow.greenjuice.model.BlogPostItem
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface GreenJuiceApiService {
    @GET("/api/posts") // <- 서비스 URL 나머지 부분 넣기
    suspend fun search_get(
        @Query("query") query: String, //검색어
        @Query("start") start: Int, //검색 시작 위치
        @Query("display") display: Int //가져올 데이터 수
    ): List<BlogPostItem>

    @POST("/api/posts") // <- 서비스 URL 나머지 부분 넣기
    suspend fun search(@Body blogRequest: BlogRequest): List<BlogPostItem>
}

@Serializable
class BlogRequest(
    private val query: String,
    private val start: Int,
    private val display: Int,
)