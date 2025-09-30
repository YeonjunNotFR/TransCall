package com.youhajun.data.room

import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.room.CreateRoomRequest
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.CurrentRoomListRequest
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.data.room.dto.toDto
import com.youhajun.data.room.dto.toEntity
import com.youhajun.data.room.dto.toModel
import com.youhajun.domain.room.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RoomRepositoryImpl @Inject constructor(
    private val remote: RoomRemoteDataSource,
    private val participantLocal: ParticipantLocalDataSource
): RoomRepository {

    override fun getRoomParticipantFlow(roomId: String): Flow<List<Participant>> {
        return participantLocal.getRoomParticipantFlow(roomId).map { it.map { it.toModel() } }
    }

    override suspend fun getRoomInfo(roomId: String): Result<RoomInfo> = runCatching {
        remote.getRoomInfo(roomId).toModel()
    }

    override suspend fun getCurrentRoomInfo(roomId: String): Result<CurrentRoomInfo> = runCatching {
        remote.getCurrentRoomInfo(roomId).toModel()
    }

    override suspend fun getCurrentRoomList(request: CurrentRoomListRequest): Result<CursorPage<CurrentRoomInfo>> = runCatching {
        remote.getCurrentRoomList(request).toModel { it.toModel() }
    }

    override suspend fun createRoom(request: CreateRoomRequest): Result<String> = runCatching {
        remote.createRoom(request.toDto())
    }

    override suspend fun joinRoomCheckByCode(roomCode: String): Result<RoomInfo> = runCatching {
        remote.joinRoomCheckByCode(roomCode).toModel()
    }

    override suspend fun upsertRoomParticipants(roomId: String, participants: List<Participant>) {
        val upsertParticipants = participants.map { it.toEntity(roomId) }
        participantLocal.upsertParticipants(upsertParticipants)
    }
}