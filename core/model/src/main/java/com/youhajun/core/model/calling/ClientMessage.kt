package com.youhajun.core.model.calling

import com.youhajun.core.model.calling.payload.RequestPayload
import com.youhajun.core.model.calling.type.MessageType

data class ClientMessage(
    val type: MessageType,
    val payload: RequestPayload,
)