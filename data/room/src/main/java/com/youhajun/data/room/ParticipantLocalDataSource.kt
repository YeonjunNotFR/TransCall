package com.youhajun.data.room

import com.youhajun.core.database.dao.ParticipantDao
import com.youhajun.core.database.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface ParticipantLocalDataSource {
    fun getRoomParticipantFlow(roomId: String): Flow<List<ParticipantEntity>>
    suspend fun upsertParticipants(participants: List<ParticipantEntity>)
}

internal class ParticipantLocalDataSourceImpl @Inject constructor(
    private val participantDao: ParticipantDao
): ParticipantLocalDataSource {

    override suspend fun upsertParticipants(participants: List<ParticipantEntity>) {
        participantDao.upsertParticipants(participants)
    }

    override fun getRoomParticipantFlow(roomId: String): Flow<List<ParticipantEntity>> {
        return participantDao.getRoomParticipantFlow(roomId)
    }
}