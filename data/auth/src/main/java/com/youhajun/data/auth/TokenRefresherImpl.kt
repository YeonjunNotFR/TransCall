package com.youhajun.data.auth

import com.youhajun.core.event.MainEvent
import com.youhajun.core.event.MainEventManager
import com.youhajun.core.network.TokenRefresher
import com.youhajun.data.common.dto.auth.JwtTokenDto
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TokenRefresherImpl @Inject constructor(
    private val mainEventManager: MainEventManager
) : TokenRefresher {

    override suspend fun refreshTokens(
        param: RefreshTokensParams,
    ): Result<BearerTokens> = runCatching {
        param.run {
            val response = client.post(AuthEndpoint.TokenRefresh.path) {
                markAsRefreshTokenRequest()
                contentType(ContentType.Application.Json)
                setBody(mapOf("refreshToken" to oldTokens?.refreshToken))
            }.body<JwtTokenDto>()

            BearerTokens(response.accessToken, response.refreshToken)
        }
    }.onFailure {
        mainEventManager.sendEvent(MainEvent.RequireLogin)
    }
}