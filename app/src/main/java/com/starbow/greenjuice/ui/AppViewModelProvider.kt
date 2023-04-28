package com.starbow.greenjuice.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.starbow.greenjuice.GreenJuiceApplication
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceAppViewModel
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceNavHostViewModel

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            GreenJuiceNavHostViewModel(greenJuiceApplication().container.greenJuiceRepository)
        }

        initializer {
            GreenJuiceAppViewModel(
                greenJuiceApplication().container.greenJuiceRepository,
                greenJuiceApplication().container.greenJuicePreferencesRepository
            )
        }
    }
}

fun CreationExtras.greenJuiceApplication(): GreenJuiceApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GreenJuiceApplication