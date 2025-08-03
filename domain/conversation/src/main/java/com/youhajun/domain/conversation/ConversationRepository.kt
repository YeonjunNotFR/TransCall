package com.youhajun.domain.conversation

import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.ConversationMessage
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun connect(roomId: String): Flow<ConversationMessage>
    suspend fun send(message: ConversationMessage)
    suspend fun close()

    fun observeConversations(roomId: String, afterTimestamp: Long): Flow<List<Conversation>>
    fun observeRecentConversation(roomId: String): Flow<Conversation>
    suspend fun upsertConversation(conversation: Conversation)
    suspend fun upsertConversations(conversations: List<Conversation>)
}