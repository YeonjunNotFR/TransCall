package com.youhajun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.youhajun.core.database.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversation WHERE roomId = :roomId AND timestamp >= :afterTimestamp ORDER BY timestamp ASC")
    fun observeConversations(roomId: String, afterTimestamp: Long): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversation WHERE roomId = :roomId ORDER BY timestamp DESC LIMIT 1")
    fun observeRecentConversation(roomId: String): Flow<ConversationEntity>

    @Upsert
    suspend fun upsert(conversationEntity: ConversationEntity)

    @Upsert
    suspend fun upsert(conversationEntities: List<ConversationEntity>)
}
