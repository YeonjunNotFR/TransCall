package com.youhajun.data.room

import com.youhajun.core.model.room.RoomInfo
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject

internal class RoomRepositoryImpl @Inject constructor(
    private val remote: RoomRemoteDataSource
): RoomRepository {

    override suspend fun createRoom(): Result<RoomInfo> = runCatching {
        remote.createRoom().toModel()
    }

    override suspend fun joinRoom(roomCode: String): Result<RoomInfo> = runCatching {
        remote.joinRoom(roomCode).toModel()
    }

    override suspend fun deleteRoom(roomCode: String) = runCatching {
        remote.deleteRoom(roomCode)
    }

    override suspend fun getRoomInfo(roomCode: String): Result<RoomInfo> = runCatching {
        remote.getRoomInfo(roomCode).toModel()
    }

    override suspend fun leaveRoom(roomCode: String) = runCatching {
        remote.leaveRoom(roomCode)
    }
}