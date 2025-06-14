package com.youhajun.data.history.dto

import com.youhajun.core.model.calling.CallHistory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CallHistoryDto(
    @SerialName("callId")
    val callId: String = "",
    @SerialName("partner")
    val partner: HistoryParticipantDto? = null,
    @SerialName("startedAt")
    val startedAt: Long = 0,
    @SerialName("durationSeconds")
    val durationSeconds: Int = 0,
    @SerialName("isFavorite")
    val isFavorite: Boolean = false,
) {
    fun toModel(): CallHistory = CallHistory(
        callId = callId,
        partner = partner?.toModel(),
        startedAtEpochSeconds = startedAt,
        durationSeconds = durationSeconds,
        isFavorite = isFavorite,
    )
}