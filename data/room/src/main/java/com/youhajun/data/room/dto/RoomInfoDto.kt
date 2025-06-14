package com.youhajun.data.room.dto

import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RoomInfoDto(
    @SerialName("code")
    val code: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("hostId")
    val hostId: String = "",
    @SerialName("participants")
    val participants: List<ParticipantDto> = emptyList(),
    @SerialName("createdAt")
    val createdAt: Long = 0,
    @SerialName("roomType")
    val roomType: String = ""
) {
    fun toModel(): RoomInfo = RoomInfo(
        code = code,
        name = name,
        hostId = hostId,
        participants = participants.map { it.toModel() }.toImmutableList(),
        createdAt = createdAt,
        roomType = RoomType.fromType(roomType)
    )
}