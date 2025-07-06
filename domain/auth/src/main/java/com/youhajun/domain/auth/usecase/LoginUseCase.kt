package com.youhajun.domain.auth.usecase

import com.youhajun.core.model.auth.LoginRequest
import com.youhajun.domain.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Result<Unit> {
        return repository.login(request)
    }
}
