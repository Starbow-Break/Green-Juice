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

    private companion object {
        val THEME = intPreferencesKey("theme")
    }

    suspend fun saveThemePreference(theme: GreenJuiceTheme) {
        dataStore.edit { preferences ->
            preferences[THEME] = theme.ordinal
        }
    }
}