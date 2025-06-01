package com.youhajun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.youhajun.core.database.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversation WHERE roomCode = :roomCode AND timestamp >= :afterTimestamp ORDER BY timestamp ASC")
    fun observeConversations(roomCode: String, afterTimestamp: Long): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversation WHERE roomCode = :roomCode ORDER BY timestamp DESC LIMIT 1")
    fun observeRecentConversation(roomCode: String): Flow<ConversationEntity>

    @Upsert
    suspend fun upsert(conversationEntity: ConversationEntity)

    @Upsert
    suspend fun upsert(conversationEntities: List<ConversationEntity>)
}
