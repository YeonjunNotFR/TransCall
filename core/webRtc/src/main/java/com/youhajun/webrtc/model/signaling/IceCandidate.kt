package com.youhajun.webrtc.model.signaling

import org.webrtc.IceCandidate

internal data class IceCandidate(
    val sdpMid: String?,
    val sdpMLineIndex: Int,
    val sdp: String,
) {
    fun toWebRtcCandidate(): IceCandidate {
        return IceCandidate(sdpMid, sdpMLineIndex, sdp)
    }
}