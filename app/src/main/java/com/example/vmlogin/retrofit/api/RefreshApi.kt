package com.example.vmlogin.retrofit.api

import com.example.vmlogin.retrofit.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

fun interface RefreshApi {
    @FormUrlEncoded
    @POST("idp/api/v1/token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
    ): Response<AuthResponse>
}