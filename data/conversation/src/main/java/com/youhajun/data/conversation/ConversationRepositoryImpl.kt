package com.youhajun.data.conversation

import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.ConversationMessage
import com.youhajun.data.conversation.dto.toDto
import com.youhajun.data.conversation.dto.toEntity
import com.youhajun.data.conversation.dto.toModel
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ConversationRepositoryImpl @Inject constructor(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource,
) : ConversationRepository {

    override fun connect(roomCode: String): Flow<ConversationMessage> =
        remote.connect(roomCode).map { it.toModel() }

    override suspend fun send(message: ConversationMessage) {
        remote.send(message.toDto())
    }

    override suspend fun close() {
        remote.close()
    }

    override fun observeConversations(roomCode: String, afterTimestamp: Long): Flow<List<Conversation>> {
        return local.observeConversations(roomCode, afterTimestamp).map { it.map { it.toModel() } }
    }

    override fun observeRecentConversation(roomCode: String): Flow<Conversation> {
        return local.observeRecentConversation(roomCode).map { it.toModel() }
    }

    override suspend fun upsertConversation(conversation: Conversation) {
        local.upsertConversation(conversation.toEntity())
    }

    override suspend fun upsertConversations(conversations: List<Conversation>) {
        local.upsertConversations(conversations.map { it.toEntity() })
    }
}