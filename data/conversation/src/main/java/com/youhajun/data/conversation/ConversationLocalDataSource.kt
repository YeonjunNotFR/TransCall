package com.youhajun.data.conversation

import androidx.paging.PagingSource
import com.youhajun.core.database.dao.ConversationCursorDao
import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.dao.ConversationMetaDao
import com.youhajun.core.database.entity.ConversationCursorEntity
import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.database.entity.ConversationMetaEntity
import com.youhajun.core.model.TimeRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface ConversationLocalDataSource {
    fun getRecentConversationFlow(roomId: String): Flow<ConversationEntity?>
    fun pagingSourceInTimeRange(roomId: String, timeRange: TimeRange): PagingSource<Int, ConversationEntity>
    suspend fun getConversationLastCursorInTimeRange(roomId: String, timeRange: TimeRange): ConversationCursorEntity?
    suspend fun upsertConversation(entity: ConversationEntity)
    suspend fun upsertAllConversations(entities: List<ConversationEntity>)
    suspend fun upsertAllConversationCursor(entities: List<ConversationCursorEntity>)
    suspend fun insertIgnoreConversationMeta(entity: ConversationMetaEntity)
    suspend fun updateConversationLastSyncAt(roomId: String, joinedAt: Long, updatedAt: Long)
    suspend fun getConversationMeta(roomId: String, joinedAt: Long): ConversationMetaEntity?
    suspend fun getConversationCursor(conversationId: String): ConversationCursorEntity?
    suspend fun deleteConversationMeta(roomId: String, joinedAt: Long)
}

internal class ConversationLocalDataSourceImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val conversationMetaDao: ConversationMetaDao,
    private val conversationCursorDao: ConversationCursorDao
): ConversationLocalDataSource {

    override fun getRecentConversationFlow(roomId: String): Flow<ConversationEntity?> =
        conversationDao.getRecentConversationFlow(roomId)

    override suspend fun getConversationLastCursorInTimeRange(roomId: String, timeRange: TimeRange): ConversationCursorEntity? {
        val lastConversation = conversationDao.getLastConversationInTimeRange(roomId, timeRange.joinedAtToEpochTime, timeRange.leftAtToEpochTime)
        return lastConversation?.id?.let { conversationCursorDao.getConversationCursor(it) }
    }

    override fun pagingSourceInTimeRange(roomId: String, timeRange: TimeRange): PagingSource<Int, ConversationEntity> =
        conversationDao.pagingSourceInTimeRange(roomId, timeRange.joinedAtToEpochTime, timeRange.leftAtToEpochTime)

    override suspend fun getConversationMeta(roomId: String, joinedAt: Long): ConversationMetaEntity? =
        conversationMetaDao.getConversationMeta(roomId, joinedAt)

    override suspend fun getConversationCursor(conversationId: String): ConversationCursorEntity? =
        conversationCursorDao.getConversationCursor(conversationId)

    override suspend fun upsertAllConversations(entities: List<ConversationEntity>) =
        conversationDao.upsertAll(entities)

    override suspend fun upsertConversation(entity: ConversationEntity) =
        conversationDao.upsert(entity)

    override suspend fun upsertAllConversationCursor(entities: List<ConversationCursorEntity>) =
        conversationCursorDao.upsertAll(entities)

    override suspend fun updateConversationLastSyncAt(roomId: String, joinedAt: Long, updatedAt: Long) =
        conversationMetaDao.updateLastSyncedAt(roomId, joinedAt, updatedAt)

    override suspend fun insertIgnoreConversationMeta(entity: ConversationMetaEntity) =
        conversationMetaDao.insertConflictIgnore(entity)

    override suspend fun deleteConversationMeta(roomId: String, joinedAt: Long) =
        conversationMetaDao.deleteMeta(roomId, joinedAt)
}