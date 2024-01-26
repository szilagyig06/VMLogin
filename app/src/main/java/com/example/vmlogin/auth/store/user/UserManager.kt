package com.example.vmlogin.auth.store.user

import com.example.vmlogin.model.UserModel

interface UserManager {
    suspend fun saveUser(user: UserModel)
    suspend fun deleteUser()
}