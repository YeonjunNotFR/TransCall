package com.youhajun.data.room.dto

import com.youhajun.core.model.room.CreateRoomRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateRoomRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("maxParticipantCount")
    val maxParticipantCount: Int,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("tags")
    val tags: Set<String>,
)

internal fun CreateRoomRequest.toDto(): CreateRoomRequestDto = CreateRoomRequestDto(
    title = title,
    maxParticipantCount = maxParticipantCount,
    visibility = visibility.type,
    tags = tags
)
