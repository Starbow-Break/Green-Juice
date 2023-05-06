package com.starbow.greenjuice.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.Event
import com.starbow.greenjuice.data.GreenJuicePreferencesRepository
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.enum.EventToastMessage
import com.starbow.greenjuice.enum.GreenJuiceTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.io.IOException

class GreenJuiceAppViewModel(
    private val greenJuiceRepository: GreenJuiceRepository,
    private val greenJuicePreferencesRepository: GreenJuicePreferencesRepository
) : ViewModel() {
    var signInState = MutableStateFlow(false)
        private set

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

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

    fun changeSignInState(state: Boolean) {
        signInState.value = state
    }

    //로그아웃
    fun signOut() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    greenJuiceRepository.signOut()
                }
                changeSignInState(result)
                _showToast.value = Event(EventToastMessage.SIGN_OUT)
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.SIGN_OUT_ERROR)
            }
        }
    }
}