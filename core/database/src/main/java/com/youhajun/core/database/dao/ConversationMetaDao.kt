package com.youhajun.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youhajun.core.database.entity.ConversationMetaEntity

@Dao
interface ConversationMetaDao {

    @Query("SELECT * FROM conversation_meta WHERE roomId = :roomId AND joinedAt = :joinedAt LIMIT 1")
    suspend fun getConversationMeta(roomId: String, joinedAt: Long): ConversationMetaEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConflictIgnore(meta: ConversationMetaEntity)

    @Query("UPDATE conversation_meta SET lastSyncedAt = :syncedAt WHERE roomId = :roomId AND joinedAt = :joinedAt")
    suspend fun updateLastSyncedAt(roomId: String, joinedAt: Long, syncedAt: Long?)

    @Query("DELETE FROM conversation_meta WHERE roomId = :roomId AND joinedAt = :joinedAt")
    suspend fun deleteMeta(roomId: String, joinedAt: Long)
}
