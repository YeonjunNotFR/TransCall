package com.youhajun.domain.auth

import com.youhajun.core.model.auth.SocialLoginRequest
import com.youhajun.core.model.auth.Nonce

interface AuthRepository {
    suspend fun socialLogin(request: SocialLoginRequest): Result<Unit>
    suspend fun hasAccessToken(): Result<Boolean>
    suspend fun getLoginNonce(): Result<Nonce>
}