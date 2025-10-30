package com.youhajun.webrtc.model.signaling

data class TurnCredential(
    val url: String,
    val username: String,
    val credential: String,
)