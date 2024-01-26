package com.example.vmlogin.retrofit.service.login

import com.example.vmlogin.retrofit.request.LoginRequest
import com.example.vmlogin.retrofit.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

fun interface LoginService {
    @POST("/idp/api/v1/token")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}