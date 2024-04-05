package com.example.careerhub.data.remote

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.careerhub.model.SignUpRespone
interface SignUpApi {
    @FormUrlEncoded
    @POST("/postRegister")
    suspend fun signUp(
        @Field("email") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("phone") phone: String
    ): Response<SignUpRespone>
}