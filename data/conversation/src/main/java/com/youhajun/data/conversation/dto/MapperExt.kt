package com.youhajun.data.conversation.dto

import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.database.entity.SenderInfoEntity
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.SenderInfo

internal fun ConversationEntity.toModel(): Conversation {
    return Conversation(
        id = id,
        roomId = roomId,
        senderInfo = senderInfo.toModel(),
        originText = originText,
        transText = transText,
        transLanguage = LanguageType.fromCode(transLanguageCode),
        timestamp = timestamp
    )
}

internal fun Conversation.toEntity(): ConversationEntity = ConversationEntity(
    id = id,
    roomId = roomId,
    senderInfo = senderInfo.toEntity(),
    originText = originText,
    transText = transText,
    transLanguageCode = transLanguage.code,
    timestamp = timestamp
)

internal fun SenderInfoEntity.toModel(): SenderInfo {
    return SenderInfo(
        senderId = id,
        displayName = displayName,
        language = LanguageType.fromCode(languageCode),
        profileUrl = profileUrl
    )
}

internal fun SenderInfo.toEntity(): SenderInfoEntity = SenderInfoEntity(
    id = senderId,
    displayName = displayName,
    languageCode = language.code,
    profileUrl = profileUrl
)