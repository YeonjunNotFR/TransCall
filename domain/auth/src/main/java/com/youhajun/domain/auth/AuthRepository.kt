package com.youhajun.domain.auth

import com.youhajun.core.model.auth.LoginRequest

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Unit>
    suspend fun hasAccessToken(): Result<Boolean>
    suspend fun getLoginNonce(): Result<String>
}