package com.youhajun.domain.room.usecase

import com.youhajun.core.model.RoomInfo
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject

class RoomInfoUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomCode: String): Result<RoomInfo> {
        return repository.getRoomInfo(roomCode)
    }
}
