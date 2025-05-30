package com.youhajun.domain.room.usecase

import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomCode: String): Result<Unit> {
        return repository.deleteRoom(roomCode)
    }
}
