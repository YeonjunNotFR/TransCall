package com.youhajun.core.model.calling.payload

sealed interface MessagePayload

sealed interface RequestPayload : MessagePayload
sealed interface ResponsePayload : MessagePayload