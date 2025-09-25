package com.youhajun.domain.room.usecase

import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentRoomInfoUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomId: String): Result<CurrentRoomInfo> {
        return repository.getCurrentRoomInfo(roomId)
    }
}
