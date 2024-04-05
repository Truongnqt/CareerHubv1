package com.example.careerhub.data.remote
import  com.example.careerhub.model.ApplyJobRespone
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import okhttp3.MultipartBody
interface ApplyJobApi {
        @FormUrlEncoded
        @POST("/applyJob")
        suspend fun applyJob(
            @Field("job_id") jobId: Int,
            @Field("jobseeker_id") jobseekerId: Int,
            @Field("CV") CV: MultipartBody.Part,
            @Field("cover_letter") coverLetter: String,

        ): Response<ApplyJobRespone>


}