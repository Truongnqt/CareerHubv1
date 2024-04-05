package com.example.careerhub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.careerhub.data.repository.JobInformationRepository
import com.example.careerhub.model.JobInformationResponse
import com.example.careerhub.model.Jobinformation
import kotlinx.coroutines.launch

class JobInformationViewModel : ViewModel() {
    private val repository: JobInformationRepository = JobInformationRepository()

    private val _jobInformation = MutableLiveData<JobInformationResponse?>()
    val jobInformation: LiveData<JobInformationResponse?> get() = _jobInformation

    private val _jobInformationById = MutableLiveData<JobInformationResponse?>()
    val jobInformationById: LiveData<JobInformationResponse?> get() = _jobInformationById
    private val _jobInformationBySuggest = MutableLiveData<JobInformationResponse?>()
    val jobInformationBySuggest: LiveData<JobInformationResponse?> get() = _jobInformationBySuggest
    private val _jobInformationPaging = MutableLiveData<JobInformationResponse?>()
    val jobInformationPaging: LiveData<JobInformationResponse?> get() = _jobInformationPaging
    private val _jobInformationByFilter = MutableLiveData<JobInformationResponse?>()
    val jobInformationByFilter: LiveData<JobInformationResponse?> get() = _jobInformationByFilter

    fun getInformation() {
        viewModelScope.launch {
            val response = repository.getInformation() {
                _jobInformation.postValue(it)
            }
        }
    }

    fun getInformationPaging() {
        viewModelScope.launch {
            val response = repository.getInformationPaging(){
            _jobInformation.postValue(it)
        }

    }
    }
    fun getInformationById(id: Int) {
        viewModelScope.launch {
            repository.getInformationById(id) { response ->
                _jobInformationById.postValue(response)
            }
        }
    }
    fun getInformationBySuggest(suggest: String) {
        viewModelScope.launch {
            repository.getInformationBySuggest(suggest) { response ->
                _jobInformationBySuggest.postValue(response)
            }
        }
    }
    fun filterJobInformation(suggest: String) {
        viewModelScope.launch {
            repository.filterJobInformation(suggest) { response ->
                _jobInformationByFilter.postValue(response)
                Log.d("JobInformationViewModel", "filterJobInformation: $response")
            }
        }
    }
}