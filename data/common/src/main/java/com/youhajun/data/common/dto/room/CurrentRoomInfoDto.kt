package com.youhajun.data.common.dto.room

import com.youhajun.core.model.room.CurrentRoomInfo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentRoomInfoDto(
    @SerialName("roomInfo")
    val roomInfo: RoomInfoDto,
    @SerialName("currentParticipants")
    val currentParticipants: List<ParticipantDto>,
) {
    fun toModel(): CurrentRoomInfo = CurrentRoomInfo(
        roomInfo = roomInfo.toModel(),
        currentParticipants = currentParticipants.map { it.toModel() }.toImmutableList()
    )
}