package com.example.vmlogin.di

import android.content.Context
import com.example.vmlogin.Constant
import com.example.vmlogin.auth.interceptor.LoginInterceptor
import com.example.vmlogin.auth.store.token.TokenManager
import com.example.vmlogin.retrofit.api.LoginApi
import com.example.vmlogin.retrofit.api.RefreshApi
import com.example.vmlogin.retrofit.service.login.LoginService
import com.example.vmlogin.retrofit.service.login.LoginServiceImpl
import com.example.vmlogin.retrofit.service.mock.MockLoginServiceImpl
import com.example.vmlogin.retrofit.service.refresh.RefreshService
import com.example.vmlogin.retrofit.service.refresh.RefreshServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Login
    @Provides
    @Singleton
    fun provideLoginApiService(api: LoginApi): LoginService {
        return LoginServiceImpl(api)
    }

//    @Provides
//    @Singleton
//    fun provideLoginApiService(@ApplicationContext context: Context): LoginService {
//        return MockLoginServiceImpl(context)
//    }

    @Provides
    @Singleton
    fun provideLoginRetrofit(@TokenLoginClient okHttpClient: OkHttpClient): LoginApi {
        return Retrofit.Builder()
            .baseUrl(Constant.Server.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    @TokenLoginClient
    fun provideLoginOkHttpClient(
        loginInterceptor: LoginInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(loginInterceptor)
            .connectTimeout(Constant.Timeout.CONNECT, TimeUnit.SECONDS)
            .readTimeout(Constant.Timeout.READ, TimeUnit.SECONDS)
            .writeTimeout(Constant.Timeout.WRITE, TimeUnit.SECONDS)
            .build()
    }
    //end region

    //Refresh
    @Provides
    @Singleton
    fun providesRefreshApiService(api: RefreshApi): RefreshService {
        return RefreshServiceImpl(api)
    }

    @Provides
    @Singleton
    fun provideRefreshRetrofit(@TokenRefreshClient okHttpClient: OkHttpClient): RefreshApi {
        return Retrofit.Builder()
            .baseUrl(Constant.Server.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RefreshApi::class.java)
    }

    @Provides
    @Singleton
    @TokenRefreshClient
    fun provideRefreshOkHttpClient(
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constant.Timeout.CONNECT, TimeUnit.SECONDS)
            .readTimeout(Constant.Timeout.READ, TimeUnit.SECONDS)
            .writeTimeout(Constant.Timeout.WRITE, TimeUnit.SECONDS)
            .build()
    }
    //end region
}