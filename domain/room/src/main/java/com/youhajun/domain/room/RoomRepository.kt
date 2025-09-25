package com.youhajun.domain.room

import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.room.CreateRoomRequest
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.CurrentRoomListRequest
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRoomParticipantFlow(roomId: String): Flow<List<Participant>>
    suspend fun getCurrentRoomList(request: CurrentRoomListRequest): Result<CursorPage<CurrentRoomInfo>>
    suspend fun getCurrentRoomInfo(roomId: String): Result<CurrentRoomInfo>
    suspend fun getRoomInfo(roomId: String): Result<RoomInfo>
    suspend fun createRoom(request: CreateRoomRequest): Result<String>
    suspend fun joinRoomCheckByCode(roomCode: String): Result<RoomInfo>
    suspend fun upsertRoomParticipants(roomId: String, participants: List<Participant>)
}