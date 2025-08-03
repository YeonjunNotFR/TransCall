package com.youhajun.data.room

import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.room.dto.RoomInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import javax.inject.Inject

internal interface RoomRemoteDataSource {
    suspend fun createRoom(request: CreateRoomRequestDto): String
    suspend fun joinRoom(roomId: String): RoomInfoDto
    suspend fun leaveRoom(roomId: String)
    suspend fun deleteRoom(roomId: String)
    suspend fun getRoomInfo(roomId: String): RoomInfoDto
}

internal class RoomRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
): RoomRemoteDataSource {

    override suspend fun createRoom(): RoomInfoDto {
        return client.post(RoomEndpoint.Create.path).body()
    }

    override suspend fun joinRoom(roomId: String): RoomInfoDto {
        return client.post(RoomEndpoint.Join(roomId).path).body()
    }

    override suspend fun deleteRoom(roomId: String) {
        return client.delete(RoomEndpoint.Delete(roomId).path).body()
    }

    override suspend fun getRoomInfo(roomId: String): RoomInfoDto {
        return client.get(RoomEndpoint.Info(roomId).path).body()
    }

    override suspend fun leaveRoom(roomId: String) {
        return client.post(RoomEndpoint.Leave(roomId).path).body()
    }
}