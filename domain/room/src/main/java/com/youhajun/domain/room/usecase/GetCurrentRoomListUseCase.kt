package com.youhajun.domain.room.usecase

import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.CurrentRoomListRequest
import com.youhajun.domain.room.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentRoomListUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(request: CurrentRoomListRequest): Result<CursorPage<CurrentRoomInfo>> {
        return repository.getCurrentRoomList(request)
    }
}
