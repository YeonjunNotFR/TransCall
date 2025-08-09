package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.calling.VideoRoomHandleInfo
import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.core.model.room.RoomInfo
import kotlinx.collections.immutable.ImmutableList

sealed interface RoomResponse : MessagePayload, ResponsePayload

data class ConnectedRoom(
    val roomInfo: RoomInfo,
    val participants: ImmutableList<CurrentParticipant>,
    val videoRoomHandleInfo: VideoRoomHandleInfo,
) : RoomResponse

data class ChangedRoom(
    val roomInfo: RoomInfo,
    val participants: ImmutableList<CurrentParticipant>,
) : RoomResponse