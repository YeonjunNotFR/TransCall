package com.youhajun.core.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit

internal object KtorHttpClient {

    fun create(
        baseUrl: String,
        tokenProvider: TokenProvider,
        tokenRefresher: TokenRefresher,
    ): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        if (BuildConfig.DEBUG) Log.d("Ktor", message)
                    }
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken()
                        val refreshToken = tokenProvider.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val tokens = tokenRefresher.refreshTokens(this)
                        tokens?.also {
                            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
                        }
                    }
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                }
            }

            engine {
                config {
                    followRedirects(true)
                    connectTimeout(60, TimeUnit.SECONDS)
                    connectionPool(ConnectionPool(10, 5, TimeUnit.MINUTES))
                }
            }
        }
    }
}
