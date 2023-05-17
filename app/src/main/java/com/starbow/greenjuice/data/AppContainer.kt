package com.starbow.greenjuice.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.starbow.greenjuice.network.GreenJuiceApiService
import retrofit2.Retrofit

interface AppContainer {
    val greenJuiceRepository: GreenJuiceRepository
    val greenJuicePreferencesRepository: GreenJuicePreferencesRepository
}

class GreenJuiceAppContainer(dataStore: DataStore<Preferences>, retrofit: Retrofit) : AppContainer {
    override val greenJuiceRepository = NetworkGreenJuiceRepository(retrofit)
    override val greenJuicePreferencesRepository = GreenJuicePreferencesRepository(dataStore)
}