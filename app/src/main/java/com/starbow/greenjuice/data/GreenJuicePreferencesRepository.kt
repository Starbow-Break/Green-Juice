package com.starbow.greenjuice.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.starbow.greenjuice.enum.GreenJuiceTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class GreenJuicePreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val themeOption: Flow<GreenJuiceTheme> = dataStore.data
        .catch {
            if(it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map { preferences ->
            GreenJuiceTheme.fromValue(preferences[THEME] ?: 2)
        }

    val accessTokenState = dataStore.data
        .catch {
            if(it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    val refreshTokenState = dataStore.data
        .catch {
            if(it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }

    companion object {
        val THEME = intPreferencesKey("theme")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveThemePreference(theme: GreenJuiceTheme) {
        dataStore.edit { preferences ->
            preferences[THEME] = theme.ordinal
        }
    }

    suspend fun saveToken(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }
}