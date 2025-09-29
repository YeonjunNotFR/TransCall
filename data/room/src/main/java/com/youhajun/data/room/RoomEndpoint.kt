package com.youhajun.data.room

sealed class RoomEndpoint(val path: String) {
    data object Create : RoomEndpoint("api/room/create")
    data object JoinRoomCheckByCode : RoomEndpoint("api/room/join-check")
    data class Info(val roomId: String) : RoomEndpoint("api/room/$roomId")
    data class OngoingInfo(val roomId: String) : RoomEndpoint("api/room/$roomId/ongoing")

    data object List : RoomEndpoint("api/room/list")
}