package com.youhajun.data.room

import com.youhajun.data.room.dto.RoomResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import javax.inject.Inject

internal interface RoomRemoteDataSource {
    suspend fun createRoom(): RoomResponseDto
    suspend fun joinRoom(roomCode: String): RoomResponseDto
    suspend fun leaveRoom(roomCode: String)
    suspend fun deleteRoom(roomCode: String)
    suspend fun getRoomInfo(roomCode: String): RoomResponseDto
}

internal class RoomRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
): RoomRemoteDataSource {

    override suspend fun createRoom(): RoomResponseDto {
        return client.post(RoomEndpoint.Create.path).body()
    }

    override suspend fun joinRoom(roomCode: String): RoomResponseDto {
        return client.post(RoomEndpoint.Join(roomCode).path).body()
    }

    override suspend fun deleteRoom(roomCode: String) {
        return client.delete(RoomEndpoint.Delete(roomCode).path).body()
    }

    override suspend fun getRoomInfo(roomCode: String): RoomResponseDto {
        return client.get(RoomEndpoint.Info(roomCode).path).body()
    }

    override suspend fun leaveRoom(roomCode: String) {
        return client.post(RoomEndpoint.Leave(roomCode).path).body()
    }
}