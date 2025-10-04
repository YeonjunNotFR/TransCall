package com.youhajun.data.history.dto

import com.youhajun.core.model.history.CallHistory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CallHistoryDto(
    @SerialName("historyId")
    val historyId: String = "",
    @SerialName("roomId")
    val roomId: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("summary")
    val summary: String = "",
    @SerialName("memo")
    val memo: String = "",
    @SerialName("isLiked")
    val isLiked: Boolean = false,
    @SerialName("joinedAtToEpochTime")
    val joinedAtToEpochTime: Long = 0,
    @SerialName("leftAtToEpochTime")
    val leftAtToEpochTime: Long? = null,
    @SerialName("durationSeconds")
    val durationSeconds: Int? = null,
    @SerialName("participants")
    val participants: List<ParticipantDto> = emptyList(),
) {
    fun toModel(): CallHistory = CallHistory(
        historyId = historyId,
        roomId = roomId,
        title = title,
        summary = summary,
        joinedAtToEpochTime = joinedAtToEpochTime,
        leftAtToEpochTime = leftAtToEpochTime,
        durationSeconds = durationSeconds,
        participants = participants.map { it.toModel() }.toImmutableList(),
        memo = memo,
        isLiked = isLiked,
    )
}