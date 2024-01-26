package com.example.vmlogin.retrofit.request

data class LoginRequest(
    val username: String,
    val password: String,
    val grantType: String,
    val clientId: String
)