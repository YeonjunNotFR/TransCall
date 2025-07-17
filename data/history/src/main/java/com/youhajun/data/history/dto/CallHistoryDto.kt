package com.youhajun.data.history.dto

import com.youhajun.core.model.calling.CallHistory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CallHistoryDto(
    @SerialName("historyId")
    val historyId: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("summary")
    val summary: String = "",
    @SerialName("memo")
    val memo: String = "",
    @SerialName("isLiked")
    val isLiked: Boolean = false,
    @SerialName("startedAt")
    val startedAt: Long = 0,
    @SerialName("endedAt")
    val endedAt: Long = 0,
    @SerialName("durationSeconds")
    val durationSeconds: Int = 0,
    @SerialName("participants")
    val participants: List<ParticipantDto> = emptyList(),
) {
    fun toModel(): CallHistory = CallHistory(
        historyId = historyId,
        title = title,
        summary = summary,
        startedAt = startedAt,
        endedAt = endedAt,
        durationSeconds = durationSeconds,
        participants = participants.map { it.toModel() }.toImmutableList(),
        memo = memo,
        isLiked = isLiked,
    )
}