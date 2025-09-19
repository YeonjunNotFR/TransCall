package com.youhajun.core.model.calling.payload

sealed interface MediaStateRequest : RequestPayload

data class MicEnableChanged(
    val isEnabled: Boolean
) : MediaStateRequest

data class CameraEnableChanged(
    val isEnabled: Boolean
) : MediaStateRequest