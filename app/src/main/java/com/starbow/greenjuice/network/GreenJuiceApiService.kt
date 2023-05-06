package com.starbow.greenjuice.network

import com.starbow.greenjuice.serializable.*
import retrofit2.http.*

interface GreenJuiceApiService {
    //검색(분석) 결과 받아오기
    @POST("/api/posts") // <- 서비스 URL 나머지 부분 넣기
    suspend fun search(@Body blogRequest: BlogRequest): List<BlogPostItem>

    //로그인
    @POST("/api/auth/login")
    suspend fun signIn(@Body account: Account): SignInResult

    //로그아웃
    @POST("/api/auth/logout")
    suspend fun signOut(): SignInResult

    //회원가입
    @POST("/api/auth/signup")
    suspend fun signUp(@Body account: Account): SignUpResult

    //아이디 중복 조회
    @POST("/api/auth/signup/{id}")
    suspend fun isIdExist(@Path("id") id: String): ExistId

    //즐겨찾기 추가
    @POST("/api/posts/{id}/bookmark")
    suspend fun addFavorites(@Path("id") postId: Int)

    //즐겨찾기 삭제
    @DELETE("/api/posts/{id}/bookmark")
    suspend fun deleteFavorites(@Path("id") postId: Int)

    //즐겨찾기 목록 조회
    @GET("/api/users/bookmarks")
    suspend fun getFavorites(): List<BlogPostItem>
}

