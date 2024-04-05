package com.example.careerhub.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careerhub.model.User
import kotlinx.coroutines.launch
import com.example.careerhub.data.repository.UserInformationRepository
import okhttp3.MultipartBody

class InformationUserViewModel : ViewModel(){
    private val repository = UserInformationRepository()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUser(email: String){
        viewModelScope.launch {
            repository.getUser(email){
                _user.postValue(it)
            }
        }
    }

}