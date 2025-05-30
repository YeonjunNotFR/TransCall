package com.youhajun.core.network.di

import com.youhajun.core.network.BuildConfig
import com.youhajun.core.network.Config
import com.youhajun.core.network.KtorHttpClient
import com.youhajun.core.network.KtorWebsocketClient
import com.youhajun.core.network.TokenProvider
import com.youhajun.core.network.TokenRefresher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    @RestHttpClient
    fun provideMainKtorHttpClient(
        tokenProvider: TokenProvider,
        tokenRefresher: TokenRefresher,
    ): HttpClient = KtorHttpClient.create(
        baseUrl = Config.REST_BASE_URL,
        tokenProvider = tokenProvider,
        tokenRefresher = tokenRefresher,
    )

    @Provides
    @Singleton
    @WebSocketHttpClient
    fun provideWebSocketKtorHttpClient(
        tokenProvider: TokenProvider,
    ): HttpClient = KtorWebsocketClient.create(
        baseUrl = Config.WEBSOCKET_BASE_URL,
        tokenProvider = tokenProvider,
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RestHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketHttpClient