package com.example.vmlogin.retrofit.service.refresh

import com.example.vmlogin.retrofit.api.RefreshApi
import com.example.vmlogin.retrofit.request.RefreshRequest
import com.example.vmlogin.retrofit.response.AuthResponse
import com.example.vmlogin.retrofit.service.BaseService
import retrofit2.Response

class RefreshServiceImpl(
    private val refreshApi: RefreshApi
) : BaseService(), RefreshService {

    override suspend fun refreshToken(request: RefreshRequest): Response<AuthResponse> {
        try {
            val response = refreshApi.refreshToken(
                refreshToken = request.refreshToken,
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
                "Connection broken. Verify that you are connected to the internet",
                e
            )
        }
    }
}