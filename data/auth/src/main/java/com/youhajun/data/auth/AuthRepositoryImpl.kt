package com.youhajun.data.auth

import com.youhajun.core.model.auth.SocialLoginRequest
import com.youhajun.core.model.auth.Nonce
import com.youhajun.data.auth.dto.toDto
import com.youhajun.domain.auth.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val local: AuthLocalDataSource
): AuthRepository {

    override suspend fun socialLogin(request: SocialLoginRequest): Result<Unit> = runCatching {
        val token = remote.socialLogin(request.toDto())
        local.saveTokens(token.accessToken, token.refreshToken)
    }

    override suspend fun hasAccessToken(): Result<Boolean> = runCatching {
        !local.getAccessToken().isNullOrBlank()
    }

    override suspend fun getLoginNonce(): Result<String> = runCatching {
        remote.getLoginNonce()
    }
}