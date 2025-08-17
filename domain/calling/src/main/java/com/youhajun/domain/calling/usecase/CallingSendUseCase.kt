package com.youhajun.domain.calling.usecase

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.domain.calling.CallingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallingSendUseCase @Inject constructor(
    private val repository: CallingRepository,
) {
    suspend operator fun invoke(msg: ClientMessage) {
        repository.send(msg)
    }
}