package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.AudioDeviceState
import com.youhajun.webrtc.model.AudioDeviceType
import kotlinx.coroutines.flow.StateFlow

interface AudioDeviceController {
    val audioDeviceState: StateFlow<AudioDeviceState>
    fun start()
    fun stop()
    fun select(device: AudioDeviceType)
}