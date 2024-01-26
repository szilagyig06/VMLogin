package com.example.vmlogin.store

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.example.vmlogin.auth.store.user.UserDataStore
import com.example.vmlogin.model.UserModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test


class UserStoreTest {

    private lateinit var userDataStore: UserDataStore
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private var dataStoreFile = appContext.preferencesDataStoreFile("auth_preferences")

    @Before
    fun setup() {
        userDataStore = UserDataStore(
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                produceFile = { dataStoreFile }
            )
        )
    }

    @After
    fun tearDown() {
        dataStoreFile.delete()
    }

    @Test
    fun saveAndDeleteUserTest() {
        val testUser = UserModel("testUser", "testRole")

        runBlocking {
            userDataStore.saveUser(testUser)
        }

        val savedUser = runBlocking { userDataStore.getUser() }

        assertNotNull(savedUser)
        assertEquals(testUser, savedUser)

        runBlocking {
            userDataStore.deleteUser()
        }

        val deleted = runBlocking { userDataStore.getUser() }

        assertEquals(UserModel("", ""), deleted)
    }
}
