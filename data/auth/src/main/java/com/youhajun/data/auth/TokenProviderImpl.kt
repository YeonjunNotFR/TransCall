package com.youhajun.data.auth

import com.youhajun.core.network.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TokenProviderImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
) : TokenProvider {

    override suspend fun deleteTokens() {
        authLocalDataSource.deleteTokens()
    }

    override suspend fun getAccessToken(): String? {
        return authLocalDataSource.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return authLocalDataSource.getRefreshToken()
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        return authLocalDataSource.saveTokens(accessToken, refreshToken)
    }
}