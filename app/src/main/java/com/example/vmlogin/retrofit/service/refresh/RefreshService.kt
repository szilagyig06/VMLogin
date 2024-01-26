package com.example.vmlogin.retrofit.service.refresh

import com.example.vmlogin.retrofit.request.RefreshRequest
import com.example.vmlogin.retrofit.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

fun interface RefreshService {
    @POST("/idp/api/v1/token")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<AuthResponse>
}