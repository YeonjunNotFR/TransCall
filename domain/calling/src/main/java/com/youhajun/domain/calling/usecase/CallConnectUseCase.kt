package com.youhajun.domain.calling.usecase

import com.youhajun.core.model.calling.CallingMessage
import com.youhajun.domain.calling.CallingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallConnectUseCase @Inject constructor(
    private val repository: CallingRepository,
) {
    operator fun invoke(roomCode: String): Flow<CallingMessage> {
        return repository.connect(roomCode)
    }
}