package com.starbow.greenjuice.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.starbow.greenjuice.network.GreenJuiceApiService

interface AppContainer {
    val greenJuiceRepository: GreenJuiceRepository
    val greenJuicePreferencesRepository: GreenJuicePreferencesRepository
}

class GreenJuiceAppContainer(dataStore: DataStore<Preferences>, api: GreenJuiceApiService) : AppContainer {
    override val greenJuiceRepository = NetworkGreenJuiceRepository(api)
    override val greenJuicePreferencesRepository = GreenJuicePreferencesRepository(dataStore)
}