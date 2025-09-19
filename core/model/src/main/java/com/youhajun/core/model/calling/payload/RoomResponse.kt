package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.calling.VideoRoomHandleInfo
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo

sealed interface RoomResponse : ResponsePayload

data class ConnectedRoom(
    val roomInfo: RoomInfo,
    val participants: List<Participant>,
    val videoRoomHandleInfo: VideoRoomHandleInfo,
) : RoomResponse

data class ChangedRoom(
    val roomInfo: RoomInfo,
    val participants: List<Participant>,
) : RoomResponse