package com.youhajun.data.room.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.CurrentParticipant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentParticipantDto(
    @SerialName("userId")
    val userId: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("language")
    val language: String = "",
) {
    fun toModel(): CurrentParticipant = CurrentParticipant(
        userId = userId,
        displayName = displayName,
        imageUrl = imageUrl ?: "",
        language = LanguageType.fromCode(language),
    )
}