package com.youhajun.domain.auth.usecase

import com.youhajun.domain.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HasAccessTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.hasAccessToken()
    }
}
