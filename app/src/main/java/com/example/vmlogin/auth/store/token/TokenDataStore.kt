package com.example.vmlogin.auth.store.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) :
    TokenManager {

    companion object {
        val ACCESS_TKN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TKN_KEY = stringPreferencesKey("refresh_token")
        val EXPIRES_IN = longPreferencesKey("expires_in")
    }

    override suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TKN_KEY] = token
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TKN_KEY] = token
        }
    }

    override suspend fun saveExpiresIn(expires: Long) {
        dataStore.edit { preferences ->
            preferences[EXPIRES_IN] = expires
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TKN_KEY]
        }.first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TKN_KEY]
        }.first()
    }

    override suspend fun getExpiresIn(): Long? {
        return dataStore.data.map { preferences ->
            preferences[EXPIRES_IN]
        }.first()
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TKN_KEY)
            preferences.remove(REFRESH_TKN_KEY)
        }
    }

    override suspend fun clearAllData() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TKN_KEY)
            preferences.remove(REFRESH_TKN_KEY)
            preferences.remove(EXPIRES_IN)
        }
    }

    override suspend fun hasToken(): Boolean {
        return getAccessToken().isNullOrBlank()
    }

    override suspend fun hasTokenExpired(): Boolean {
        if (!hasToken()) {
            return false
        }

        val expiryTime = getExpiresIn()
        val currentTime = System.currentTimeMillis()

        return expiryTime?.let { currentTime > it } ?: false

    }
}