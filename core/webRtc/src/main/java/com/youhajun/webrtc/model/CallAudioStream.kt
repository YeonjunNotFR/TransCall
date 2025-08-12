package com.youhajun.webrtc.model

import org.webrtc.AudioTrack

sealed interface CallAudioStream {
    val key get() = CallMediaKey.createKey(userId, mediaContentType)
    val userId: String
    val isSpeaking: Boolean
    val audioTrack: AudioTrack?
    val isMicEnabled: Boolean
    val mediaContentType: String
}

data class LocalAudioStream(
    override val userId: String,
    override val audioTrack: AudioTrack?,
    override val isSpeaking: Boolean = false,
    override val isMicEnabled: Boolean = true,
    override val mediaContentType: String,
    val isMute: Boolean = false,
    val selectedDevice: AudioDeviceType = AudioDeviceType.NONE,
    val availableDevices: Set<AudioDeviceType> = emptySet()
) : CallAudioStream

data class RemoteAudioStream(
    override val userId: String,
    override val audioTrack: AudioTrack?,
    override val isSpeaking: Boolean = false,
    override val isMicEnabled: Boolean = true,
    override val mediaContentType: String,
    val isOutputEnabled: Boolean = true,
) : CallAudioStream