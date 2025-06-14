package com.youhajun.data.history.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HistoryParticipantDto(
    @SerialName("userId")
    val userId: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("language")
    val language: String = "",
) {
    fun toModel(): Participant = Participant(
        userId = userId,
        displayName = displayName,
        imageUrl = imageUrl,
        language = LanguageType.fromCode(language),
    )
}