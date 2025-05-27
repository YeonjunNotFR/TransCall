package com.youhajun.data.auth

import android.util.Log
import com.youhajun.core.network.TokenRefresher
import com.youhajun.data.auth.dto.TokenRefreshResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

internal class TokenRefresherImpl @Inject constructor() : TokenRefresher {

    override suspend fun refreshTokens(
        param: RefreshTokensParams,
    ): BearerTokens? {
        param.apply {
            try {
                val response = client.post(AuthEndpoint.TokenRefresh.path) {
                    markAsRefreshTokenRequest()
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("refresh_token" to oldTokens?.refreshToken))
                }.body<TokenRefreshResponse>()

                BearerTokens(response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                Log.e("Ktor", "Token refresh failed", e)
            }
            return null
        }
    }
}