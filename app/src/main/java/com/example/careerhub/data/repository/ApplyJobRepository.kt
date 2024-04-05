package com.example.careerhub.data.repository

import com.example.careerhub.data.remote.ApplyJobApi
import com.example.careerhub.data.remote.SignUpApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.careerhub.model.ApplyJobRespone
import android.util.Log
import okhttp3.MultipartBody

class ApplyJobRepository {

    val retrofitIntance = Retrofit.Builder().baseUrl("http://139.59.242.16/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(ApplyJobApi::class.java)

        suspend fun applyJob(jobId: Int, jobseekerId: Int, CV: MultipartBody.Part, coverLetter: String): Response<ApplyJobRespone> {
            Log.d("ApplyJobRepository", "applyJob: $jobId")
            return retrofitIntance.applyJob(jobId, jobseekerId, CV, coverLetter)
        }


}