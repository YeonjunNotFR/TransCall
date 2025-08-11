package com.youhajun.data.calling

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.data.calling.dto.toDto
import com.youhajun.domain.calling.CallingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CallingRepositoryImpl @Inject constructor(
    private val remote: CallingDataSource
) : CallingRepository {

    override fun connect(roomId: String): Flow<ServerMessage> {
        return remote.connect(roomId).map { it.toModel() }
    }

    override suspend fun send(message: ClientMessage) {
        remote.send(message.toDto())
    }

    override suspend fun close() {
        remote.close()
    }
}