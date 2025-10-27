package com.youhajun.data.common.dto.calling.payload

import com.youhajun.core.model.calling.payload.ChangedRoom
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.data.common.dto.calling.VideoRoomHandleInfoDto
import com.youhajun.data.common.dto.calling.payload.ChangedRoomDto.Companion.CHANGED_ROOM_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.ConnectedRoomDto.Companion.CONNECTED_ACTION_TYPE
import com.youhajun.data.common.dto.room.ParticipantDto
import com.youhajun.data.common.dto.room.RoomInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface RoomResponseDto : ResponsePayloadDto

@Serializable
@SerialName(CONNECTED_ACTION_TYPE)
data class ConnectedRoomDto(
    @SerialName("roomInfo")
    val roomInfoDto: RoomInfoDto,
    @SerialName("participants")
    val participants: List<ParticipantDto>,
    @SerialName("videoRoomHandleInfo")
    val videoRoomHandleInfo: VideoRoomHandleInfoDto,
) : RoomResponseDto {

    companion object {
        const val CONNECTED_ACTION_TYPE: String = "connected"
    }

    override fun toModel(): ResponsePayload = ConnectedRoom(
        roomInfo = roomInfoDto.toModel(),
        participants = participants.map { it.toModel() },
        videoRoomHandleInfo = videoRoomHandleInfo.toModel(),
    )
}

@Serializable
@SerialName(CHANGED_ROOM_ACTION_TYPE)
data class ChangedRoomDto(
    @SerialName("roomInfo")
    val roomInfoDto: RoomInfoDto,
    @SerialName("participants")
    val participants: List<ParticipantDto>,
) : RoomResponseDto {

    companion object {
        const val CHANGED_ROOM_ACTION_TYPE: String = "changedRoom"
    }

    override fun toModel(): ResponsePayload = ChangedRoom(
        roomInfo = roomInfoDto.toModel(),
        participants = participants.map { it.toModel() },
    )
}