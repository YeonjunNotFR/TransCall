package com.youhajun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.youhajun.core.database.entity.ConversationCursorEntity

@Dao
interface ConversationCursorDao {

    @Upsert
    suspend fun upsertAll(entities: List<ConversationCursorEntity>)

    @Query("SELECT * FROM conversation_cursor WHERE conversationId = :conversationId LIMIT 1")
    suspend fun getConversationCursor(conversationId: String): ConversationCursorEntity?
}
