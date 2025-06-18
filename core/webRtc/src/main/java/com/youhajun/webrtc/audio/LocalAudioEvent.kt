package com.youhajun.webrtc.audio

sealed interface LocalAudioEvent {
    data class MicEnableChanged(val enabled: Boolean) : LocalAudioEvent
}
