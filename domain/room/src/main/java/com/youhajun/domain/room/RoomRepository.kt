package com.youhajun.domain.room

import com.youhajun.core.model.room.RoomInfo

interface RoomRepository {
    suspend fun createRoom(): Result<RoomInfo>
    suspend fun joinRoom(roomCode: String): Result<RoomInfo>
    suspend fun leaveRoom(roomCode: String): Result<Unit>
    suspend fun deleteRoom(roomCode: String): Result<Unit>
    suspend fun getRoomInfo(roomCode: String): Result<RoomInfo>
}