package com.youhajun.data.conversation

sealed class ConversationEndpoint(val path: String) {
    data class Conversation(val roomId: String) : ConversationEndpoint("/conversation/$roomId")
    data class List(val roomId: String): ConversationEndpoint("/conversation/$roomId")
}