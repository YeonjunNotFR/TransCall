package com.youhajun.data.conversation

import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface ConversationLocalDataSource {
    fun observeConversations(roomCode: String, afterTimestamp: Long): Flow<List<ConversationEntity>>
    fun observeRecentConversation(roomCode: String): Flow<ConversationEntity>
    suspend fun upsertConversation(conversation: ConversationEntity)
    suspend fun upsertConversations(conversations: List<ConversationEntity>)
}

internal class ConversationLocalDataSourceImpl @Inject constructor(
    private val dao: ConversationDao
) : ConversationLocalDataSource {

    override fun observeConversations(roomCode: String, afterTimestamp: Long): Flow<List<ConversationEntity>> {
        return dao.observeConversations(roomCode, afterTimestamp)
    }

    override fun observeRecentConversation(roomCode: String): Flow<ConversationEntity> {
        return dao.observeRecentConversation(roomCode)
    }

    override suspend fun upsertConversations(conversations: List<ConversationEntity>) {
        dao.upsert(conversations)
    }

    override suspend fun upsertConversation(conversation: ConversationEntity) {
        dao.upsert(conversation)
    }
}