package com.youhajun.domain.room.usecase

import com.youhajun.core.model.room.RoomInfo
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomInfoUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomCode: String): Result<RoomInfo> {
        return repository.getRoomInfo(roomCode)
    }
}
