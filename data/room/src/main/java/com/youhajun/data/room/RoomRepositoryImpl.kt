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

    override suspend fun joinRoom(roomId: String): Result<RoomInfo> = runCatching {
        remote.joinRoom(roomId).toModel()
    }

    override suspend fun deleteRoom(roomId: String) = runCatching {
        remote.deleteRoom(roomId)
    }

    override suspend fun getRoomInfo(roomId: String): Result<RoomInfo> = runCatching {
        remote.getRoomInfo(roomId).toModel()
    }

    override suspend fun leaveRoom(roomId: String) = runCatching {
        remote.leaveRoom(roomId)
    }
}