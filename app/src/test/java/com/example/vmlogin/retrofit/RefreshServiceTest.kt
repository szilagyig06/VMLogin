package com.example.vmlogin.retrofit

import android.util.Log
import com.example.vmlogin.Constant
import com.example.vmlogin.retrofit.request.RefreshRequest
import com.example.vmlogin.retrofit.service.refresh.RefreshService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RefreshServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var refreshService: RefreshService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        refreshService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshService::class.java)
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
    fun refresh_success() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJsonFromFile("auth_success.json"))

        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val response = refreshService.refreshToken(
                RefreshRequest(
                    refreshToken = "NTBhN2RhMWQtZmUwNy00YzE0LThiMWItMDA3NzM3ZjQ3NzYzIyNkNmQ5OTViZS1jY2IxLTQ0MGUtODM4NS1lOTkwMTEwMzBhYzA=",
                    grantType = Constant.App.GRANT_TYPE_PW,
                    clientId = Constant.App.CLIENT_ID
                )
            )

            assert(response.isSuccessful)
            assert(response.body()?.accessToken == "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZHA6dXNlcl9pZCI6IjUwYTdkYTFkLWZlMDctNGMxNC04YjFiLTAwNzczN2Y0Nzc2MyIsImlkcDp1c2VyX25hbWUiOiJqZG9lIiwiaWRwOmZ1bGxuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiZWRpdG9yIiwiZXhwIjoxNTU2NDc2MjU1fQ.iqFmotBtfAYLplfpLVh_kPgvOIPyV7UMm-NZA06XA5I")
        }
    }

    @Test
    fun refresh_error_401() {
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody("{ \"error\": \"Invalid username or password\" }")

        mockWebServer.enqueue(mockResponse)

        runBlocking {
            val response = refreshService.refreshToken(
                RefreshRequest(
                    refreshToken = "NTBhN2RhMWQtZmUwNy00YzE0LThiMWItMDA3NzM3ZjQ3NzYzIyNkNmQ5OTViZS1jY2IxLTQ0MGUtODM4NS1lOTkwMTEwMzBhYzA=",
                    grantType = Constant.App.GRANT_TYPE_PW,
                    clientId = Constant.App.CLIENT_ID
                )
            )

            assert(!response.isSuccessful)
            assert(response.code() == 401)
        }
    }

    private fun getJsonFromFile(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val source = inputStream!!.source()
        val buffer = source.buffer()
        return buffer.readUtf8()
    }
}