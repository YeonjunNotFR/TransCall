package com.youhajun.core.model.calling

sealed interface CallingMessageType {
    data object Connect: CallingMessageType

    data class Joined(val participants: List<String>) : CallingMessageType

    data class LeftResponse(val userId: String) : CallingMessageType

    data object LeftRequest : CallingMessageType
}