package com.youhajun.webrtc.model

data class MicChunk(
    val audioFormat: Int,
    val channelCount: Int,
    val sampleRate: Int,
    val audioData: ByteArray
)