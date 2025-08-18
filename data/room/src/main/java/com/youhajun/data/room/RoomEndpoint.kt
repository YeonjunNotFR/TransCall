package com.youhajun.data.room

sealed class RoomEndpoint(val path: String) {
    data object Create : RoomEndpoint("api/room/create")
    data object JoinRoomByCode : RoomEndpoint("api/room/join")
    data class Info(val roomId: String) : RoomEndpoint("/room/$roomId")
    data class Leave(val roomId: String) : RoomEndpoint("/room/$roomId/leave")
    data class Delete(val roomId: String) : RoomEndpoint("/room/$roomId")
}