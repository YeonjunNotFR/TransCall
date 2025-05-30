package com.youhajun.core.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json

internal object KtorWebsocketClient {

    fun create(
        baseUrl: String,
        tokenProvider: TokenProvider,
    ): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = 20000
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
