package com.youhajun.data.conversation

import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface ConversationLocalDataSource {
    fun observeConversations(roomId: String, afterTimestamp: Long): Flow<List<ConversationEntity>>
    fun observeRecentConversation(roomId: String): Flow<ConversationEntity>
    suspend fun upsertConversation(conversation: ConversationEntity)
    suspend fun upsertConversations(conversations: List<ConversationEntity>)
}

internal class ConversationLocalDataSourceImpl @Inject constructor(
    private val dao: ConversationDao
) : ConversationLocalDataSource {

    override fun observeConversations(roomId: String, afterTimestamp: Long): Flow<List<ConversationEntity>> {
        return dao.observeConversations(roomId, afterTimestamp)
    }

    override fun observeRecentConversation(roomId: String): Flow<ConversationEntity> {
        return dao.observeRecentConversation(roomId)
    }

    override suspend fun upsertConversations(conversations: List<ConversationEntity>) {
        dao.upsert(conversations)
    }

    override suspend fun upsertConversation(conversation: ConversationEntity) {
        dao.upsert(conversation)
    }
}