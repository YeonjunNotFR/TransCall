package com.youhajun.webrtc.model.local

import com.youhajun.webrtc.model.stream.AudioDeviceType

internal data class AudioDeviceState(
    val selectedDevice: AudioDeviceType = AudioDeviceType.NONE,
    val availableDevices: Set<AudioDeviceType> = emptySet()
)