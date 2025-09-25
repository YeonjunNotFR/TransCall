package com.youhajun.domain.room.usecase

import com.youhajun.core.model.room.Participant
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertRoomParticipantsUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomId: String, participants: List<Participant>) {
        return repository.upsertRoomParticipants(roomId, participants)
    }
}