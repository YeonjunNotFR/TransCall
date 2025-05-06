package com.youhajun.data.room.dto

import com.youhajun.core.model.RoomInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RoomResponseDto(
    @SerialName("code")
    val code: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("hostId")
    val hostId: String = "",
    @SerialName("participants")
    val participants: List<ParticipantDto> = emptyList(),
    @SerialName("createdAt")
    val createdAt: Long = 0
) {
    fun toModel(): RoomInfo = RoomInfo(
        code = code,
        name = name,
        hostId = hostId,
        participants = participants.map { it.toModel() },
        createdAt = createdAt
    )
}