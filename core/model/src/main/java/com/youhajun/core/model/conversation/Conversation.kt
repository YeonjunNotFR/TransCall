package com.youhajun.core.model.conversation

import com.youhajun.core.model.LanguageType

data class Conversation(
    val id: String,
    val roomCode: String,
    val senderInfo: SenderInfo,
    val originText: String,
    val transText: String?,
    val transLanguage: LanguageType,
    val timestamp: Long,
): ConversationMessageType