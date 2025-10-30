package com.youhajun.webrtc.model.signaling

import org.webrtc.RtpCapabilities

data class RtpCodec(
    val mimeType: String,
    val clockRate: Int,
    val channels: Int? = null,
    val parameters: Map<String, Any>? = null
)

internal fun RtpCapabilities.CodecCapability.toRtpCodec(): RtpCodec {
    return RtpCodec(
        mimeType = mimeType,
        clockRate = clockRate,
        channels = numChannels?.takeIf { it > 0 },
        parameters = parameters ?: emptyMap()
    )
}