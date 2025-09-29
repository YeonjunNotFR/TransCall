package com.youhajun.data.room

import com.youhajun.core.model.room.CurrentRoomListRequest
import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.common.pagination.CursorPageDto
import com.youhajun.data.common.parametersFrom
import com.youhajun.data.room.dto.CreateRoomRequestDto
import com.youhajun.data.room.dto.CurrentRoomInfoDto
import com.youhajun.data.room.dto.RoomInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

internal interface RoomRemoteDataSource {
    suspend fun getCurrentRoomList(request: CurrentRoomListRequest): CursorPageDto<CurrentRoomInfoDto>
    suspend fun createRoom(request: CreateRoomRequestDto): String
    suspend fun joinRoomCheckByCode(roomCode: String): RoomInfoDto
    suspend fun getRoomInfo(roomId: String): RoomInfoDto
    suspend fun getCurrentRoomInfo(roomId: String): CurrentRoomInfoDto
}

internal class RoomRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
) : RoomRemoteDataSource {
    override suspend fun getCurrentRoomList(request: CurrentRoomListRequest): CursorPageDto<CurrentRoomInfoDto> {
        return client.get(RoomEndpoint.List.path) {
            parameter("createdAtSort", request.createdAtSort.type)
            parameter("participantSort", request.participantSort.type)
            parametersFrom(request.cursorRequest)
        }.body()
    }

    override suspend fun getCurrentRoomInfo(roomId: String): CurrentRoomInfoDto {
        return client.get(RoomEndpoint.OngoingInfo(roomId).path).body()
    }

    override suspend fun createRoom(request: CreateRoomRequestDto): String {
        return client.post(RoomEndpoint.Create.path) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun joinRoomCheckByCode(roomCode: String): RoomInfoDto {
        return client.get(RoomEndpoint.JoinRoomCheckByCode.path) {
            parameter("roomCode", roomCode)
        }.body()
    }

    override suspend fun getRoomInfo(roomId: String): RoomInfoDto {
        return client.get(RoomEndpoint.Info(roomId).path).body()
    }
}