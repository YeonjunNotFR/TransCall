package com.youhajun.webrtc.model

sealed interface LocalAudioEvent {
    data class MicEnableChanged(val enabled: Boolean) : LocalAudioEvent
}
