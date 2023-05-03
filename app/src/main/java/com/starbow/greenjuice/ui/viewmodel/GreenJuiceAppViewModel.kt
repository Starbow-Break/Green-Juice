package com.starbow.greenjuice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.data.GreenJuicePreferencesRepository
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.data.SampleDataSource
import com.starbow.greenjuice.enum.GreenJuiceTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.starbow.greenjuice.NO_ACCOUNT
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GreenJuiceAppViewModel(
    private val greenJuiceRepository: GreenJuiceRepository,
    private val greenJuicePreferencesRepository: GreenJuicePreferencesRepository
) : ViewModel() {
     var curAccount by mutableStateOf("")
        private set

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

    //로그인
    fun signIn(id: String, password: String): Boolean {
        val result = SampleDataSource.authentication(id, password)
        if(result) curAccount = id
        return result
    }

    //로그아웃
    fun signOut() {
        curAccount = NO_ACCOUNT
    }

    //로그인 한 계정 유무 확인
    fun isSignIn(): Boolean {
        return curAccount != NO_ACCOUNT
    }

    //회원 가입
    fun signUp(id: String, password: String) {
        /*viewModelScope.launch {
            greenJuiceRepository.signUp(id, password)
        }*/
        SampleDataSource.addAccount(id, password)
    }

    //중복 아이디 확인
    fun isDuplicatedId(id: String): Boolean {
        /*viewModelScope.launch {
            greenJuiceRepository.signUp(id, password)
        }*/
        return SampleDataSource.isExistId(id)
    }
}