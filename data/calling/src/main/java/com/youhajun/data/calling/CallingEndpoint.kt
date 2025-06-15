package com.youhajun.data.calling

sealed class CallingEndpoint(val path: String) {
    data class Calling(val roomCode: String) : CallingEndpoint("/signaling/$roomCode")
}