package com.example.careerhub.data.remote
import com.example.careerhub.model.WorkSkillRespone
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkSkillApi {
    @GET("/getSkillByEmail")
    suspend fun getSkillByEmail(@Query("email") email: String): Response<WorkSkillRespone>
}