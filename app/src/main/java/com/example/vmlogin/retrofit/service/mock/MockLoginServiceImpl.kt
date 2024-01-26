package com.example.vmlogin.retrofit.service.mock

import android.content.Context
import com.example.vmlogin.helper.ResourceHelper
import com.example.vmlogin.retrofit.request.LoginRequest
import com.example.vmlogin.retrofit.response.AuthResponse
import com.example.vmlogin.retrofit.service.BaseService
import com.example.vmlogin.retrofit.service.login.LoginService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class MockLoginServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseService(), LoginService {

    override suspend fun login(request: LoginRequest): Response<AuthResponse> {
        val mockResponse = ResourceHelper.getAuthResponse(context)
        return Response.success(mockResponse)
    }
}