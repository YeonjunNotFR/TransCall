package com.youhajun.domain.room

import com.youhajun.core.model.room.CreateRoomRequest
import com.youhajun.core.model.room.RoomInfo

interface RoomRepository {
    suspend fun createRoom(request: CreateRoomRequest): Result<String>
    suspend fun joinRoomByCode(roomCode: String): Result<RoomInfo>
    suspend fun leaveRoom(roomId: String): Result<Unit>
    suspend fun deleteRoom(roomId: String): Result<Unit>
    suspend fun getRoomInfo(roomId: String): Result<RoomInfo>
}