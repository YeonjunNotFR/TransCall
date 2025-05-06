package com.youhajun.domain.room.usecase

import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomCode: String): Result<Unit> {
        return repository.leaveRoom(roomCode)
    }
}
