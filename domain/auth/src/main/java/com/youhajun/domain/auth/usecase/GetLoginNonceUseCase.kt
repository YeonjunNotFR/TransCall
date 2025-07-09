package com.youhajun.domain.auth.usecase

import com.youhajun.core.model.auth.Nonce
import com.youhajun.domain.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLoginNonceUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Nonce> {
        return authRepository.getLoginNonce()
    }
}
