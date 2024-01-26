package com.example.vmlogin.helper

import android.content.Context
import android.util.Log
import com.example.vmlogin.R
import com.example.vmlogin.retrofit.response.AuthResponse
import com.google.gson.Gson
import okio.BufferedSource
import okio.buffer
import okio.source

object ResourceHelper {
    private const val TAG = "ResourceHelper"

    fun getAuthResponse(context: Context): AuthResponse? {
        try {
            val inputStream = context.resources.openRawResource(R.raw.auth_success)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()

            return gson.fromJson(jsonString, AuthResponse::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "getAuthResponse: ${e.message}", e)
        }
        return null
    }

    fun getAuthResponseJson(context: Context): String {
        try {
            val inputStream = context.resources.openRawResource(R.raw.auth_success)

            inputStream?.let {
                val source = inputStream.source()
                return source.buffer().readUtf8()
            }

        } catch (e: Exception) {
            Log.e(TAG, "getAuthResponse: ${e.message}", e)
        }
        return ""
    }

    fun getJsonFromFile(fileName: String): String {
        return getBufferedSource(fileName)?.readUtf8() ?: ""
    }

    private fun getBufferedSource(fileName: String): BufferedSource? {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        try {
            inputStream?.let {
                val source = inputStream.source()
                return source.buffer()
            }

        } catch (e: Exception) {
            Log.e(TAG, "getBufferedSource: ${e.message}", e)
        }

        return null
    }
}