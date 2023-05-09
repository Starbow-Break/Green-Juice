package com.starbow.greenjuice.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.starbow.greenjuice.GreenJuiceApplication
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceAppViewModel
import com.starbow.greenjuice.ui.viewmodel.GreenJuiceNavHostViewModel
import com.starbow.greenjuice.ui.viewmodel.SignInViewModel
import com.starbow.greenjuice.ui.viewmodel.SignUpViewModel

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            GreenJuiceNavHostViewModel(
                greenJuiceApplication().container.greenJuiceRepository,
                greenJuiceApplication().container.greenJuicePreferencesRepository
            )
        }

        initializer {
            GreenJuiceAppViewModel(
                greenJuiceApplication().container.greenJuiceRepository,
                greenJuiceApplication().container.greenJuicePreferencesRepository
            )
        }

        initializer {
            SignUpViewModel(greenJuiceApplication().container.greenJuiceRepository)
        }

        initializer {
            SignInViewModel(
                greenJuiceApplication().container.greenJuiceRepository,
                greenJuiceApplication().container.greenJuicePreferencesRepository
            )
        }
    }
}

fun CreationExtras.greenJuiceApplication(): GreenJuiceApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GreenJuiceApplication