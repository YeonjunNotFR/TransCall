package com.youhajun.webrtc.model

sealed interface LocalVideoEvent {
    data class EnabledChanged(val enabled: Boolean) : LocalVideoEvent
}
