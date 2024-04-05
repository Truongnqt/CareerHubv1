package com.example.careerhub.data.repository

import android.util.Log
import com.example.careerhub.data.remote.JobInformationApi
import com.example.careerhub.model.JobInformationResponse
import com.example.careerhub.model.Jobinformation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

class JobInformationRepository {
    val retrofitInstance = Retrofit.Builder().baseUrl("http://139.59.242.16/")
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(JobInformationApi::class.java)

    suspend fun getInformation(listJobInformation: (JobInformationResponse) -> Unit) {
        val response = retrofitInstance.getJobInformation()
        if (response.isSuccessful) {
            response.body()?.let { listJobInformation(it) }
        }
    }

    suspend fun getInformationPaging(listJobInformation: (JobInformationResponse) -> Unit) {
        val response = retrofitInstance.getJobInformationPaging()
        if (response.isSuccessful) {
            response.body()?.let { listJobInformation(it) }
        }
    }

    suspend fun getInformationById(id: Int, jobInformation: (JobInformationResponse) -> Unit) {
        Log.d("JobInformationRepository", "getInformationById: $id")
        val response = retrofitInstance.getJobInformationById(id)
        if (response.isSuccessful) {
            Log.d("JobInformationRepository", "getInformationById: ${response.body()}")
            response.body()?.let { jobInformation(it) }
        }
    }
    suspend fun getInformationBySuggest(suggest: String, jobInformation: (JobInformationResponse) -> Unit) {
        val response = retrofitInstance.getJobInformationBySuggest(suggest)
        if (response.isSuccessful) {
            response.body()?.let { jobInformation(it) }
        }
    }
    suspend fun filterJobInformation(suggest: String, jobInformation: (JobInformationResponse) -> Unit) {
        val response = retrofitInstance.filterJobInformation(suggest)
        if (response.isSuccessful) {
            response.body()?.let { jobInformation(it) }
        }
    }

}