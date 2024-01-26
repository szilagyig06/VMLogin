package com.example.vmlogin.retrofit

import android.util.Log
import com.example.vmlogin.Constant
import com.example.vmlogin.helper.ResourceHelper
import com.example.vmlogin.retrofit.request.LoginRequest
import com.example.vmlogin.retrofit.service.login.LoginService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var loginApi: LoginService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        loginApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginService::class.java)
    }

    @After
    fun tearDown() {
        try {
            mockWebServer.shutdown()
        } catch (e: Exception) {
            Log.e("Test", "Error in tearDown(): ${e.message}")
        }
    }

    @Test
    fun login_success() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(ResourceHelper.getJsonFromFile("auth_success.json"))

        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val response = loginApi.login(
                LoginRequest(
                    username = "userName",
                    password = "password",
                    grantType = Constant.App.GRANT_TYPE_PW,
                    clientId = Constant.App.CLIENT_ID
                )
            )

            assert(response.isSuccessful)
            assert(response.body()?.accessToken == "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZHA6dXNlcl9pZCI6IjUwYTdkYTFkLWZlMDctNGMxNC04YjFiLTAwNzczN2Y0Nzc2MyIsImlkcDp1c2VyX25hbWUiOiJqZG9lIiwiaWRwOmZ1bGxuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiZWRpdG9yIiwiZXhwIjoxNTU2NDc2MjU1fQ.iqFmotBtfAYLplfpLVh_kPgvOIPyV7UMm-NZA06XA5I")
        }
    }

    @Test
    fun login_error_401() {
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody("{ \"error\": \"Invalid username or password\" }")

        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val response = loginApi.login(
                LoginRequest(
                    username = "userName",
                    password = "password",
                    grantType = Constant.App.GRANT_TYPE_PW,
                    clientId = Constant.App.CLIENT_ID
                )
            )

            assert(!response.isSuccessful)
            assert(response.code() == 401)
        }
    }


}