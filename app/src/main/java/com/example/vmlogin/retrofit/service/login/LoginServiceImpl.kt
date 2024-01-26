package com.example.vmlogin.retrofit.service.login

import com.example.vmlogin.retrofit.api.LoginApi
import com.example.vmlogin.retrofit.request.LoginRequest
import com.example.vmlogin.retrofit.response.AuthResponse
import com.example.vmlogin.retrofit.service.BaseService
import retrofit2.Response

class LoginServiceImpl(
    private val loginApi: LoginApi
) : BaseService(), LoginService {

    override suspend fun login(request: LoginRequest): Response<AuthResponse> {
        try {
            val response = loginApi.login(
                username = request.username,
                password = request.password,
                grantType = request.grantType,
                clientId = request.clientId
            )

            if (response.isSuccessful) {
                return response
            } else {
                if (response.code() == 401) {
                    throw InvalidDataError(
                        "Invalid username or password",
                        Throwable(response.errorBody().toString())
                    )
                } else {
                    throw UnexpectedError(
                        "Unexpected error",
                        Throwable(response.errorBody().toString())
                    )
                }
            }
        } catch (e: Exception) {
            throw ConnectionError(
                "Connection broken. Verify that you are connected to the internet", e
            )
        }
    }
}