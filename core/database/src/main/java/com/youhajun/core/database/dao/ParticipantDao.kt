package com.youhajun.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.youhajun.core.database.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {

    @Query("SELECT * FROM participant WHERE roomId = :roomId AND leftAtToEpochTime IS NULL")
    fun getCurrentRoomParticipantFlow(roomId: String): Flow<List<ParticipantEntity>>

    @Upsert
    suspend fun upsertParticipants(participants: List<ParticipantEntity>)
}
