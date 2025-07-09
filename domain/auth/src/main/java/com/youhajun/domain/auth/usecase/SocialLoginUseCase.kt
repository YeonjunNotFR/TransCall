package com.youhajun.domain.auth.usecase

import com.youhajun.core.model.auth.SocialLoginRequest
import com.youhajun.domain.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: SocialLoginRequest): Result<Unit> {
        return repository.socialLogin(request)
    }
}
