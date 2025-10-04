package com.example.signup

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("resume")
    suspend fun getResume(@Query("name") name: String): Response<Resume>
}
