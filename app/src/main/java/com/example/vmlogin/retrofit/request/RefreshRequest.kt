package com.example.vmlogin.retrofit.request

data class RefreshRequest(
    val refreshToken: String,
    val grantType: String,
    val clientId: String
)