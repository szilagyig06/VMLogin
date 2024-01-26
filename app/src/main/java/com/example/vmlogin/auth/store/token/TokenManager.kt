package com.example.vmlogin.auth.store.token

interface TokenManager {
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveExpiresIn(expires: Long)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getExpiresIn(): Long?
    suspend fun clearAllTokens()
    suspend fun clearAllData()
    suspend fun hasToken(): Boolean
    suspend fun hasTokenExpired(): Boolean
}