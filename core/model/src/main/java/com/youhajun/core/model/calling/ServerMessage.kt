package com.youhajun.core.model.calling

import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.type.MessageType

data class ServerMessage(
    val type: MessageType,
    val payload: ResponsePayload,
    val timestamp: Long,
    val senderId: String?,
)