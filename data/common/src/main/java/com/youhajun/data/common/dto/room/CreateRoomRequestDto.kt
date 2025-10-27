package com.youhajun.data.common.dto.room

import com.youhajun.core.model.room.CreateRoomRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("maxParticipantCount")
    val maxParticipantCount: Int,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("tags")
    val tags: Set<String>,
)

fun CreateRoomRequest.toDto(): CreateRoomRequestDto = CreateRoomRequestDto(
    title = title,
    maxParticipantCount = maxParticipantCount,
    visibility = visibility.type,
    tags = tags
)
