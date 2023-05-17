package com.starbow.greenjuice.data

import com.starbow.greenjuice.model.JuiceItem
import com.starbow.greenjuice.network.GreenJuiceApiService
import com.starbow.greenjuice.serializable.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

interface GreenJuiceRepository {
    suspend fun search(query: String, start: Int, display: Int): List<JuiceItem>
    suspend fun signIn(id: String, password: String): Response<SignInResult>
    suspend fun signOut(accessToken: String, refreshToken: String): Boolean
    suspend fun getNewToken(accessToken: String, refreshToken: String): Response<TokenResponse>
    suspend fun signUp(id: String, password: String): Boolean
    suspend fun isIdExist(id: String): Boolean
    suspend fun getFavorites(accessToken: String, refreshToken: String): Response<List<BlogPostItem>>
    suspend fun addFavorites(accessToken: String, refreshToken: String, postId: Int): Response<Unit>
    suspend fun deleteFavorites(accessToken: String, refreshToken: String, postId: Int): Response<Unit>
    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse?
}
class NetworkGreenJuiceRepository(
    private val retrofit: Retrofit
) : GreenJuiceRepository {
    private val greenJuiceApiService = retrofit.create(GreenJuiceApiService::class.java)

    override suspend fun search(query: String, start: Int, display: Int): List<JuiceItem> {
        return greenJuiceApiService.search(BlogRequest(query, start, display)).map { it.toJuiceItem() }
    }

    override suspend fun signIn(id: String, password: String): Response<SignInResult> {
        return greenJuiceApiService.signIn(Account(id, password))
    }

    override suspend fun signOut(accessToken: String, refreshToken: String): Boolean {
        return greenJuiceApiService.signOut("Bearer $accessToken", "refresh=$refreshToken").getLogout()
    }

    override suspend fun getNewToken(accessToken: String, refreshToken: String): Response<TokenResponse> {
        return greenJuiceApiService.getNewToken("Bearer $accessToken", "refresh=$refreshToken")
    }

    override suspend fun signUp(id: String, password: String): Boolean {
        val message = greenJuiceApiService.signUp(Account(id, password))
        return message.getMessage() == "회원가입 완료"
    }

    override suspend fun isIdExist(id: String): Boolean {
        return greenJuiceApiService.isIdExist(id).toBoolean()
    }

    override suspend fun getFavorites(accessToken: String, refreshToken: String): Response<List<BlogPostItem>> {
        return greenJuiceApiService.getFavorites("Bearer $accessToken", "refresh=$refreshToken")
    }

    override suspend fun addFavorites(accessToken: String, refreshToken: String, postId: Int): Response<Unit> {
        return greenJuiceApiService.addFavorites("Bearer $accessToken", "refresh=$refreshToken", postId)
    }

    override suspend fun deleteFavorites(accessToken: String, refreshToken: String, postId: Int): Response<Unit> {
        return greenJuiceApiService.deleteFavorites("Bearer $accessToken", "refresh=$refreshToken", postId)
    }

    override fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
        return retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(errorBody)
    }
}