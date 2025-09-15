package com.youhajun.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.youhajun.core.database.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Upsert
    suspend fun upsertAll(conversations: List<ConversationEntity>)

    @Upsert
    suspend fun upsert(conversation: ConversationEntity)

    @Query(
        """
        DELETE FROM conversation WHERE roomId = :roomId
        AND createdAtToEpochTime >= :joined AND (:left IS NULL OR createdAtToEpochTime <= :left)
        """
    )
    suspend fun clearTimeRange(roomId: String, joined: Long, left: Long?)

    @Query(
        """
        SELECT * FROM conversation WHERE roomId = :roomId
        AND createdAtToEpochTime >= :joined AND (:left IS NULL OR createdAtToEpochTime <= :left)
        ORDER BY createdAtToEpochTime ASC
        """
    )
    fun pagingSourceInTimeRange(
        roomId: String,
        joined: Long,
        left: Long?
    ): PagingSource<Int, ConversationEntity>

    @Query("SELECT * FROM conversation WHERE roomId = :roomId ORDER BY createdAtToEpochTime DESC LIMIT 1")
    fun getRecentConversationFlow(roomId: String): Flow<ConversationEntity?>

    @Query(
        """
        SELECT * FROM conversation WHERE roomId = :roomId 
        AND createdAtToEpochTime >= :joined AND (:left IS NULL OR createdAtToEpochTime <= :left)
        ORDER BY createdAtToEpochTime DESC LIMIT 1
        """
    )
    suspend fun getLastConversationInTimeRange(
        roomId: String,
        joined: Long,
        left: Long?
    ): ConversationEntity?
}
