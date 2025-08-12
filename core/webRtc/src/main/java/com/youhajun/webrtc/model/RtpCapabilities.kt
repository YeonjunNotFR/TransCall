package com.youhajun.webrtc.model

data class RtpCapabilities(
    val codecs: List<RtpCodec>,
    val headerExtensions: List<Any>? = null
)