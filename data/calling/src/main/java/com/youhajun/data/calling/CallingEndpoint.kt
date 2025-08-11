package com.youhajun.data.calling

sealed class CallingEndpoint(val path: String) {
    data object Calling : CallingEndpoint("/ws/room")
}