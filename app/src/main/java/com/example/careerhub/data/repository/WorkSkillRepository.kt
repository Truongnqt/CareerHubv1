package com.example.careerhub.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.careerhub.data.remote.WorkSkillApi
import com.example.careerhub.model.WorkSkillRespone

class WorkSkillRepository {
    val retrofitInstance = Retrofit.Builder().baseUrl("http://139.59.242.16/")
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(WorkSkillApi::class.java)
    suspend fun getSkill(email: String, workSkillInformation: (WorkSkillRespone) -> Unit){
        val response = retrofitInstance.getSkillByEmail(email)
        if (response.isSuccessful) {
            response.body()?.let {
                workSkillInformation(it)
            }
        }
    }
}