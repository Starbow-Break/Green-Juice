package com.starbow.greenjuice.ui.viewmodel

import android.util.Log
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
    private val greenJuicePrefRepo: GreenJuicePreferencesRepository
) : ViewModel() {
    var signInState = MutableStateFlow(false)
        private set

    private var navBackBlockedId: Int? = null
    var navigateBackEnabled = MutableStateFlow(true)
        private set

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

    val themeState: StateFlow<GreenJuiceTheme>
        = greenJuicePrefRepo.themeOption.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GreenJuiceTheme.SYSTEM
        )

    var accessTokenStateFlow = greenJuicePrefRepo.accessTokenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
        private set
    var refreshTokenStateFlow = greenJuicePrefRepo.refreshTokenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
        private set

    fun updateThemeOption(themeOption: GreenJuiceTheme) {
        viewModelScope.launch {
            greenJuicePrefRepo.saveThemePreference(themeOption)
        }
    }

    fun changeSignInState(state: Boolean) {
        signInState.value = state
    }

    //로그아웃
    fun signOut(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    greenJuiceRepository.signOut(accessToken, refreshToken)
                }
                changeSignInState(!result)
                _showToast.value = Event(EventToastMessage.SIGN_OUT)
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.SIGN_OUT_ERROR)
            }
        }
    }

    fun navBackBlocked(id: Int) {
        if(navigateBackEnabled.value) {
            navigateBackEnabled.value = false
            navBackBlockedId = id
        }
    }

    fun navBackWakeup(id: Int) {
        if(!navigateBackEnabled.value && navBackBlockedId == id) {
            navigateBackEnabled.value = true
            navBackBlockedId = null
        }
    }

    fun requestRefuse() {
        _showToast.value = Event(EventToastMessage.REFUSE)
    }
}