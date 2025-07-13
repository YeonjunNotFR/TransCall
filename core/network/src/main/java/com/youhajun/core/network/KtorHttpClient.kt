package com.youhajun.core.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
internal class KtorHttpClient @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val tokenRefresher: TokenRefresher,
) {

    fun createHttps(baseUrl: String): HttpClient = HttpClient(OkHttp) {
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
                    tokenRefresher.refreshTokens(this@refreshTokens).onSuccess {
                        val refreshToken = it.refreshToken
                        if(refreshToken != null) tokenProvider.saveTokens(it.accessToken, refreshToken)
                    }.onFailure {
                        tokenProvider.deleteTokens()
                    }.getOrNull()
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

    fun createWss(baseUrl: String): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = 20000.milliseconds
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        if (BuildConfig.DEBUG) Log.d("Ktor", message)
                    }
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.WSS
                    host = baseUrl
                }
            }

            engine {
                requestTimeout = 60000
                maxConnectionsCount = 10
            }
        }
    }
}
