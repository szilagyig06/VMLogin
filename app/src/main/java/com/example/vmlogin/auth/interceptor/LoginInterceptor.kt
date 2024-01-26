package com.example.vmlogin.auth.interceptor

import com.example.vmlogin.auth.store.token.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class LoginInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val response = chain.proceed(request)

        val responseBody = response.body?.string()
        val tokenJson = try {
            JSONObject(responseBody)
        } catch (e: JSONException) {
            null
        }

        if (tokenJson != null && tokenJson.has("access_token")) {
            val accessToken = tokenJson.getString("access_token")
            val refreshToken = tokenJson.getString("refresh_token")
            val expiresIn = tokenJson.getInt("expires_in")

            val now = Date()
            val calendar = Calendar.getInstance()
            calendar.time = now
            calendar.add(Calendar.SECOND, expiresIn)

            val expiry = calendar.timeInMillis

            runBlocking {
                tokenManager.saveAccessToken(accessToken)
                tokenManager.saveRefreshToken(refreshToken)
                tokenManager.saveExpiresIn(expiry)
            }
        }

        return response
    }

}