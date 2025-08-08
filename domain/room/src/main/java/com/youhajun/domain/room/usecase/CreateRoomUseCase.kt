package com.youhajun.domain.room.usecase

import com.youhajun.core.model.room.CreateRoomRequest
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(request: CreateRoomRequest): Result<String> {
        return repository.createRoom(request)
    }
}
