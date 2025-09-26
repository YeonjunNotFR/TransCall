package com.youhajun.data.conversation.dto

import com.youhajun.core.database.entity.ConversationCursorEntity
import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.pagination.CursorPage

internal fun ConversationEntity.toModel() = TranslationMessage(
    conversationId = id,
    roomId = roomId,
    senderId = senderId,
    originText = originText,
    originLanguage = LanguageType.fromCode(originLanguageCode),
    transText = transText,
    transLanguage = transLanguageCode?.let { LanguageType.fromCode(it) },
    updatedAtToEpochTime = updatedAtToEpochTime,
    createdAtToEpochTime = createdAtToEpochTime
)

internal fun TranslationMessage.toEntity() = ConversationEntity(
    id = conversationId,
    roomId = roomId,
    senderId = senderId,
    originText = originText,
    originLanguageCode = originLanguage.code,
    transText = transText,
    transLanguageCode = transLanguage?.code,
    updatedAtToEpochTime = updatedAtToEpochTime,
    createdAtToEpochTime = createdAtToEpochTime
)

internal fun CursorPage<TranslationMessage>.toEntities(): List<ConversationEntity> =
    edges.map { it.node.toEntity() }

internal fun CursorPage<TranslationMessage>.toCursorEntities(): List<ConversationCursorEntity> =
    edges.map { ConversationCursorEntity(it.node.conversationId, it.cursor) }