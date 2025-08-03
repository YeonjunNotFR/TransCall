package com.youhajun.domain.calling

import com.youhajun.core.model.calling.CallingMessage
import kotlinx.coroutines.flow.Flow

interface CallingRepository {
    fun connect(roomId: String): Flow<CallingMessage>
    suspend fun send(message: CallingMessage)
    suspend fun close()
}