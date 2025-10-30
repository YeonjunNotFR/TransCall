package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.local.AudioDeviceState
import com.youhajun.webrtc.model.stream.AudioDeviceType
import kotlinx.coroutines.flow.StateFlow

internal interface AudioDeviceController {
    val audioDeviceState: StateFlow<AudioDeviceState>
    fun start()
    fun stop()
    fun select(device: AudioDeviceType)
}