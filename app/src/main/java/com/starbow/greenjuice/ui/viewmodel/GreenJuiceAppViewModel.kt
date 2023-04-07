package com.starbow.greenjuice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.data.GreenJuicePreferencesRepository
import com.starbow.greenjuice.enum.GreenJuiceTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GreenJuiceAppViewModel(
    private val greenJuicePreferencesRepository: GreenJuicePreferencesRepository
) : ViewModel() {
    val themeState: StateFlow<GreenJuiceTheme>
        = greenJuicePreferencesRepository.themeOption.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GreenJuiceTheme.SYSTEM
        )

    fun updateThemeOption(themeOption: GreenJuiceTheme) {
        viewModelScope.launch {
            greenJuicePreferencesRepository.saveThemePreference(themeOption)
        }
    }
}