package com.youhajun.data.common.dto.room

import com.youhajun.core.database.entity.ParticipantEntity
import com.youhajun.core.model.CountryType
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    @SerialName("participantId")
    val participantId: String = "",
    @SerialName("userId")
    val userId: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("languageCode")
    val languageCode: String = "",
    @SerialName("countryCode")
    val countryCode: String = "",
) {
    fun toModel(): Participant = Participant(
        participantId = participantId,
        displayName = displayName,
        imageUrl = imageUrl ?: "",
        userId = userId,
        language = LanguageType.fromCode(languageCode),
        country = CountryType.fromCode(countryCode)
    )
}

fun Participant.toEntity(roomId: String) = ParticipantEntity(
    participantId = participantId,
    roomId = roomId,
    userId = userId,
    displayName = displayName,
    languageCode = language.code,
    countryCode = country.code,
    imageUrl = imageUrl,
)

fun ParticipantEntity.toModel() = Participant(
    participantId = participantId,
    userId = userId,
    displayName = displayName,
    language = LanguageType.fromCode(languageCode),
    country = CountryType.fromCode(countryCode),
    imageUrl = imageUrl ?: "",
)