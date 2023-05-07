package com.starbow.greenjuice.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbow.greenjuice.Event
import com.starbow.greenjuice.data.GreenJuiceRepository
import com.starbow.greenjuice.enum.EventToastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SignInViewModel(
    private val greenJuiceRepository: GreenJuiceRepository
) : ViewModel() {
    var isLoading = MutableStateFlow(false)
        private set

    private val _showToast = MutableLiveData<Event<EventToastMessage>>()
    val showToast: LiveData<Event<EventToastMessage>> = _showToast

    //로그인
    fun signIn(id: String, password: String) {
        if(isLoading.value) {
            _showToast.value = Event(EventToastMessage.REFUSE)
            return
        }

        isLoading.value = true

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    greenJuiceRepository.signIn(id, password)
                }
                if(result) {
                    _showToast.value = Event(EventToastMessage.SIGN_IN)
                    Log.d("Account", "Sign in Success! Account ID : $id")
                } else {
                    _showToast.value = Event(EventToastMessage.SIGN_IN_FAIL)
                }

            } catch(e: IOException) {
                _showToast.value = Event(EventToastMessage.SIGN_IN_ERROR)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun requestRefuse() {
        _showToast.value = Event(EventToastMessage.REFUSE)
    }
}