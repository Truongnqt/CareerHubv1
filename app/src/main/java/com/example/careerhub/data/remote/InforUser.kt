package com.example.careerhub.data.remote

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query
import com.example.careerhub.model.User
import okhttp3.MultipartBody

interface InforUser {

@GET("/getJobSeekerbyEmail")
suspend fun getUser(@Query("email") email: String): Response<User>
@GET("/postcreateJobSeeker")
suspend fun postUser(@Query("name") name: String, @Query("avatar") avatar: MultipartBody.Part,
                     @Query("phone") phone: String, @Query("address")
                     address: String, @Query("national") national: String)
: Response<User>
}