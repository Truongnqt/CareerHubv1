package com.example.careerhub.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careerhub.model.SignUpRespone
import com.example.careerhub.data.repository.AuthenticationRepository
import com.example.careerhub.data.repository.SignUpRepository
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val repository : SignUpRepository = SignUpRepository()
    private val _signUp = MutableLiveData<SignUpRespone>()
    val signUp: LiveData<SignUpRespone> get() = _signUp

    private val _registerUser = MutableLiveData<String>()
    val registerUser: LiveData<String> get() = _registerUser


    fun SignUp(email: String, password: String, name: String,phone: String) {
        viewModelScope.launch {
            val response = repository.signUp(email, password, name, phone)
            _signUp.value = response.body()
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(email, password) {
                _registerUser.postValue(it)
            }
        }
    }
}