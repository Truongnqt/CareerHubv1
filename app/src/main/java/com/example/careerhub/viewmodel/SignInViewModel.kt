package com.example.careerhub.viewmodel

import com.example.careerhub.model.AuthenResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careerhub.data.repository.AuthenticationRepository
import kotlinx.coroutines.launch


class SignInViewModel : ViewModel() {
    private val repository: AuthenticationRepository = AuthenticationRepository()
    private val _signIn = MutableLiveData<AuthenResponse>()
    val signIn: LiveData<AuthenResponse> get() = _signIn

    private val _forgotPassword = MutableLiveData<String>()
    val forgotPassword: LiveData<String> get() = _forgotPassword

    fun SignIn(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.signIn(email, password)
            _signIn.value = response.body()
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            repository.forgotPassword(email) {
                _forgotPassword.postValue(it)
            }
        }
    }
}