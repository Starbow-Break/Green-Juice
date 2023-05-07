package com.starbow.greenjuice.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.Event
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.enum.EventToastMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SignUpViewModel(
    private val greenJuiceRepository: GreenJuiceRepository
) : ViewModel() {
    var isValid = MutableStateFlow(false)
        private set
    var isValidLoading = MutableStateFlow(false)
        private set
    var signUpLoading = MutableStateFlow(false)
        private set

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

    fun isIdExist(id: String) {
        if(isValidLoading.value) {
            _showToast.value = Event(EventToastMessage.REFUSE)
            return
        }

        isValidLoading.value = true

        viewModelScope.launch {
            try {
                val result = greenJuiceRepository.isIdExist(id)
                isValid.value = !result
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.NETWORK_ERROR)
            } finally {
                isValidLoading.value = false
            }
        }
    }

    //회원 가입
    fun signUp(id: String, password: String) {
        if(signUpLoading.value) {
            _showToast.value = Event(EventToastMessage.REFUSE)
            return
        }

        signUpLoading.value = true

        viewModelScope.launch {
            try {
                greenJuiceRepository.signUp(id, password)
                _showToast.value = Event(EventToastMessage.SIGN_UP)
            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.SIGN_UP_ERROR)
            } finally {
                signUpLoading.value = false
            }
        }
    }

    fun requestRefuse() {
        _showToast.value = Event(EventToastMessage.REFUSE)
    }
}