package com.youhajun.core.model.conversation

data class ConversationMessage(
    val type: ConversationMessageType,
    val from: String,
    val timestamp: Long,
)