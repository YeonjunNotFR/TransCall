package com.youhajun.domain.room.usecase

import com.youhajun.core.model.room.Participant
import com.youhajun.domain.room.RoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRoomParticipantFlowUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(roomId: String): Flow<List<Participant>> {
        return repository.getRoomParticipantFlow(roomId)
    }
}