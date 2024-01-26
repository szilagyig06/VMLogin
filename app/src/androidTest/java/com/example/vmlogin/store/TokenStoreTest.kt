package com.example.vmlogin.store

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.example.vmlogin.auth.store.token.TokenDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TokenStoreTest {
    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var tokenDataStore: TokenDataStore
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private var dataStoreFile = appContext.preferencesDataStoreFile("auth_preferences")

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { dataStoreFile }
        )
        tokenDataStore = TokenDataStore(dataStore)
    }

    @After
    fun tearDown() {
        dataStoreFile.delete()
    }

    @Test
    fun tokenStoreTest() = runBlocking {
        val token = "testAccessToken"

        var saved = tokenDataStore.getAccessToken()
        Assert.assertTrue(saved.isNullOrBlank())

        tokenDataStore.saveAccessToken(token)

        saved = tokenDataStore.getAccessToken()
        assertEquals(token, saved)

        tokenDataStore.clearAllTokens()
        saved = tokenDataStore.getAccessToken()
        Assert.assertTrue(saved.isNullOrBlank())
    }
}
