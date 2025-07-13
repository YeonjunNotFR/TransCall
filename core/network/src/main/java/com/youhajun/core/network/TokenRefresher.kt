package com.youhajun.core.network

import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams

interface TokenRefresher {
    suspend fun refreshTokens(param: RefreshTokensParams): Result<BearerTokens>
}