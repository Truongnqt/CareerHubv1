package com.example.careerhub.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careerhub.data.remote.ApplyJobApi
import com.example.careerhub.data.repository.ApplyJobRepository
import com.example.careerhub.model.ApplyJobRespone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import okhttp3.MultipartBody
class ApplyJobViewModel: ViewModel() {
    private val repository = ApplyJobRepository()
    private val _applyJobResponse = MutableLiveData<ApplyJobRespone>()
    val applyJobResponse: LiveData<ApplyJobRespone> get() = _applyJobResponse

    fun applyJob(jobId: Int, jobseekerId: Int, CV: MultipartBody.Part, coverLetter: String) {
        viewModelScope.launch {
            val response = repository.applyJob(jobId, jobseekerId, CV, coverLetter)
            if (response.isSuccessful) {
                _applyJobResponse.value = response.body()
            } else {
                Log.d("ApplyJobViewModel", "applyJob: ${response.message()}")
            }
        }
    }
    }