package com.youhajun.data.conversation

sealed class ConversationEndpoint(val path: String) {
    data class List(val roomId: String) : ConversationEndpoint("api/call/room/$roomId/conversations")
    data class Sync(val roomId: String) : ConversationEndpoint("api/call/room/$roomId/conversations/sync")
}