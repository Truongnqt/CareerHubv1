package com.example.careerhub.data.remote

import com.example.careerhub.model.JobInformationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query
import com.example.careerhub.model.Jobinformation

interface JobInformationApi {
    @GET("/getJobInformation")
    suspend fun getJobInformation(): Response<JobInformationResponse>
    @GET("/getJobInformationPaging")
    suspend fun getJobInformationPaging(): Response<JobInformationResponse>
    @GET("/getJobInformationById/{id}")
    suspend fun getJobInformationById(@Path("id") id: Int): Response<JobInformationResponse>
    @GET("/getJobInformationBySuggest")
    suspend fun getJobInformationBySuggest(@Query("job_name") suggest: String): Response<JobInformationResponse>
    @GET("/filterJobInformation")
    suspend fun filterJobInformation(@Query("suggest") suggest: String): Response<JobInformationResponse>
}