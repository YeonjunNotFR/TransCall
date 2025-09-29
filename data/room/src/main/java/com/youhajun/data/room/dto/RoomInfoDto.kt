package com.youhajun.data.room.dto

import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.core.model.room.RoomStatus
import com.youhajun.core.model.room.RoomVisibility
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomInfoDto(
    @SerialName("roomId")
    val roomId: String,
    @SerialName("roomCode")
    val roomCode: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("maxParticipantCount")
    val maxParticipantCount: Int = 0,
    @SerialName("currentParticipantCount")
    val currentParticipantCount: Int = 0,
    @SerialName("visibility")
    val visibility: String = "",
    @SerialName("joinType")
    val joinType: String = "",
    @SerialName("tags")
    val tags: Set<String> = emptySet(),
    @SerialName("status")
    val status: String = "",
    @SerialName("hostId")
    val hostId: String = "",
    @SerialName("createdAtToEpochTime")
    val createdAtToEpochTime: Long = 0,
) {
    fun toModel(): RoomInfo = RoomInfo(
        roomId = roomId,
        roomCode = roomCode,
        title = title,
        maxParticipantCount = maxParticipantCount,
        currentParticipantCount = currentParticipantCount,
        visibility = RoomVisibility.fromType(visibility),
        joinType = RoomJoinType.fromType(joinType),
        tags = tags.toImmutableSet(),
        status = RoomStatus.fromType(status),
        hostId = hostId,
        createdAtToEpochTime = createdAtToEpochTime,
    )
}