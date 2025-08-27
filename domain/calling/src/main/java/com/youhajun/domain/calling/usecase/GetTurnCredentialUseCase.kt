package com.youhajun.domain.calling.usecase

import com.youhajun.core.model.calling.TurnCredential
import com.youhajun.domain.calling.CallingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTurnCredentialUseCase @Inject constructor(
    private val repository: CallingRepository,
) {
    suspend operator fun invoke(): Result<TurnCredential> {
        return repository.getTurnCredential()
    }
}