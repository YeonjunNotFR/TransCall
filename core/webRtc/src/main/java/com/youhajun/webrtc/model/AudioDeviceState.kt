package com.youhajun.webrtc.model

data class AudioDeviceState(
    val selectedDevice: AudioDeviceType = AudioDeviceType.NONE,
    val availableDevices: Set<AudioDeviceType> = emptySet()
)