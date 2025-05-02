package com.youhajun.core.network

import android.util.Log
import com.youhajun.core.network.model.TokenRefreshResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit

internal object KtorHttpClient {

    fun create(
        baseUrl: String,
        tokenRefreshEndpoint: String,
        tokenProvider: TokenProvider,
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
                        try {
                            val response = client.post(tokenRefreshEndpoint) {
                                markAsRefreshTokenRequest()
                                contentType(ContentType.Application.Json)
                                setBody(mapOf("refresh_token" to oldTokens?.refreshToken))
                            }.body<TokenRefreshResponse>()

                            tokenProvider.saveTokens(response.accessToken, response.refreshToken)
                            BearerTokens(response.accessToken, response.refreshToken)
                        } catch (e: Exception) {
                            Log.e("Ktor", "Token refresh failed", e)
                            null
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
