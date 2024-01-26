package com.example.vmlogin.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenLoginClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenRefreshClient