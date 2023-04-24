package com.starbow.greenjuice

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.starbow.greenjuice.data.AppContainer
import com.starbow.greenjuice.data.GreenJuiceAppContainer
import com.starbow.greenjuice.network.GreenJuiceApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:4000" // <- Node.js 서버 URL 넣기
private const val THEME_PREFERENCE_NAME = "theme_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = THEME_PREFERENCE_NAME
)

@OptIn(ExperimentalSerializationApi::class)
class GreenJuiceApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build()

        val retrofitService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(GreenJuiceApiService::class.java)

        container = GreenJuiceAppContainer(dataStore, retrofitService)
    }
}