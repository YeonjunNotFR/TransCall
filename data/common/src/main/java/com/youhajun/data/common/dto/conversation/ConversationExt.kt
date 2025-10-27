package com.youhajun.data.common.dto.conversation

import com.youhajun.core.database.entity.ConversationCursorEntity
import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.conversation.ConversationState
import com.youhajun.core.model.pagination.CursorPage

fun ConversationEntity.toModel() = TranslationMessage(
    conversationId = id,
    roomId = roomId,
    senderId = senderId,
    state = ConversationState.fromType(state),
    originText = originText,
    originLanguage = LanguageType.fromCode(originLanguageCode),
    transText = transText,
    transLanguage = transLanguageCode?.let { LanguageType.fromCode(it) },
    updatedAtToEpochTime = updatedAtToEpochTime,
    createdAtToEpochTime = createdAtToEpochTime
)

fun TranslationMessage.toEntity() = ConversationEntity(
    id = conversationId,
    roomId = roomId,
    senderId = senderId,
    state = state.type,
    originText = originText,
    originLanguageCode = originLanguage.code,
    transText = transText,
    transLanguageCode = transLanguage?.code,
    updatedAtToEpochTime = updatedAtToEpochTime,
    createdAtToEpochTime = createdAtToEpochTime
)

fun CursorPage<TranslationMessage>.toEntities(): List<ConversationEntity> =
    edges.map { it.node.toEntity() }

fun CursorPage<TranslationMessage>.toCursorEntities(): List<ConversationCursorEntity> =
    edges.map { ConversationCursorEntity(it.node.conversationId, it.cursor) }