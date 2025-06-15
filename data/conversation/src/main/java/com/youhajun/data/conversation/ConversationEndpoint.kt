package com.youhajun.data.conversation

sealed class ConversationEndpoint(val path: String) {
    data class Conversation(val roomCode: String) : ConversationEndpoint("/conversation/$roomCode")
    data class List(val roomCode: String): ConversationEndpoint("/conversation/$roomCode")
}