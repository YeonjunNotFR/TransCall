package com.youhajun.data.auth

import com.youhajun.core.network.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TokenProviderImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
) : TokenProvider {

    override suspend fun getAccessToken(): String? {
        return authLocalDataSource.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return authLocalDataSource.getRefreshToken()
    }
}