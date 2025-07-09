package com.youhajun.domain.auth

import com.youhajun.core.model.auth.LoginRequest
import com.youhajun.core.model.auth.SocialLoginRequest

interface AuthRepository {
    suspend fun socialLogin(request: SocialLoginRequest): Result<Unit>
    suspend fun hasAccessToken(): Result<Boolean>
    suspend fun getLoginNonce(): Result<String>
}