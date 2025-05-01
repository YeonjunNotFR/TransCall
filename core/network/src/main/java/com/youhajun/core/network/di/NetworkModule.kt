package com.youhajun.core.network.di

import com.youhajun.core.network.BuildConfig
import com.youhajun.core.network.KtorHttpClient
import com.youhajun.core.network.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMainKtorHttpClient(
        tokenProvider: TokenProvider,
    ): HttpClient = KtorHttpClient.create(
        baseUrl = BuildConfig.BASE_URL,
        tokenProvider = tokenProvider,
        tokenRefreshEndpoint = BuildConfig.TOKEN_REFRESH_ENDPOINT,
    )
}