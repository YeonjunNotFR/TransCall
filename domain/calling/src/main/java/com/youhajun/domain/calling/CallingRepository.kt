package com.youhajun.domain.calling

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.TurnCredential
import kotlinx.coroutines.flow.Flow

interface CallingRepository {
    fun connect(roomId: String): Flow<ServerMessage>
    suspend fun send(message: ClientMessage)
    suspend fun close()
    suspend fun getTurnCredential(): Result<TurnCredential>
}