package com.youhajun.data.conversation.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.SenderInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SenderInfoDto(
    @SerialName("id")
    val id: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("languageCode")
    val languageCode: String,
    @SerialName("profileUrl")
    val profileUrl: String? = null,
) {
    fun toModel(): SenderInfo {
        return SenderInfo(
            senderId = id,
            displayName = displayName,
            language = LanguageType.fromCode(languageCode),
            profileUrl = profileUrl
        )
    }
}

internal fun SenderInfo.toDto(): SenderInfoDto = SenderInfoDto(
    id = senderId,
    displayName = displayName,
    languageCode = language.code,
    profileUrl = profileUrl
)