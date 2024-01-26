package com.example.vmlogin.auth.store.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vmlogin.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) :
    UserManager {

    companion object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    val userModelFlow: Flow<UserModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val username = preferences[USERNAME] ?: ""
            val userRole = preferences[USER_ROLE] ?: ""
            UserModel(username, userRole)
        }

    override suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = user.username
            preferences[USER_ROLE] = user.userRole
        }
    }

    suspend fun getUser(): UserModel {
        return dataStore.data.map { preferences ->
            val username = preferences[USERNAME] ?: ""
            val userRole = preferences[USER_ROLE] ?: ""
            UserModel(username, userRole)
        }.first()
    }

    suspend fun getUserName(): String {
        return dataStore.data.map { preferences ->
            preferences[USERNAME] ?: ""
        }.first()
    }

    override suspend fun deleteUser() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME)
            preferences.remove(USER_ROLE)
        }
    }
}