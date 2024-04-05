package com.example.careerhub.data.repository

import com.example.careerhub.data.remote.InforUser
import com.example.careerhub.data.remote.JobInformationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.careerhub.model.User
class UserInformationRepository {
    val retrofitInstance = Retrofit.Builder().baseUrl("http://139.59.242.16/")
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(InforUser::class.java)

    suspend fun getUser(email: String, userInformation: (User) -> Unit){
        val response = retrofitInstance.getUser(email)
        if (response.isSuccessful) {
            response.body()?.let {
                userInformation(it)
            }
        }
    }
}