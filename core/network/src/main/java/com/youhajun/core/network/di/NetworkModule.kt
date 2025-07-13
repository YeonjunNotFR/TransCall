package com.youhajun.core.network.di

import com.youhajun.core.network.Config
import com.youhajun.core.network.KtorHttpClient
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
        ktorHttpClient: KtorHttpClient
    ): HttpClient = ktorHttpClient.createHttps(baseUrl = Config.REST_BASE_URL)

    @Provides
    @Singleton
    @WebSocketHttpClient
    fun provideWebSocketKtorHttpClient(
        ktorHttpClient: KtorHttpClient
    ): HttpClient = ktorHttpClient.createWss(baseUrl = Config.WEBSOCKET_BASE_URL)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RestHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketHttpClient