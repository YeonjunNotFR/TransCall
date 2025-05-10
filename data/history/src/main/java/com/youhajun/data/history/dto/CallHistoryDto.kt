package com.youhajun.data.history.dto

import com.youhajun.core.model.CallHistory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CallHistoryDto(
    @SerialName("callId")
    val callId: String = "",
    @SerialName("partnerName")
    val partnerName: String = "",
    @SerialName("partnerImageUrl")
    val partnerImageUrl: String? = null,
    @SerialName("startedAt")
    val startedAt: Long = 0,
    @SerialName("durationSeconds")
    val durationSeconds: Int = 0,
    @SerialName("isFavorite")
    val isFavorite: Boolean = false,
) {
    fun toModel(): CallHistory = CallHistory(
        callId = callId,
        partnerName = partnerName,
        partnerImageUrl = partnerImageUrl,
        startedAtEpochSeconds = startedAt,
        durationSeconds = durationSeconds,
        isFavorite = isFavorite,
    )
}