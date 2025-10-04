package com.youhajun.domain.calling.usecase

import com.youhajun.domain.calling.CallingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallingSendBinaryUseCase @Inject constructor(
    private val repository: CallingRepository,
) {
    suspend operator fun invoke(msg: ByteArray) {
        repository.sendBinaryMessage(msg)
    }
}