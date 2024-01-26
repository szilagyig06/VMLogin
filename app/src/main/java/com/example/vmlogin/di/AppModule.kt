package com.example.vmlogin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.vmlogin.auth.store.token.TokenDataStore
import com.example.vmlogin.auth.store.token.TokenManager
import com.example.vmlogin.auth.store.user.UserDataStore
import com.example.vmlogin.auth.store.user.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val AUTH_PREFERENCES = "auth_preferences"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Data Store
    @Provides
    @Singleton
    fun provideTokenManager(dataStore: DataStore<Preferences>): TokenManager {
        return TokenDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideUserManager(dataStore: DataStore<Preferences>): UserManager {
        return UserDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { appContext.preferencesDataStoreFile(AUTH_PREFERENCES) }
        )
    }
    //end region

    //Context
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
    //end region
}