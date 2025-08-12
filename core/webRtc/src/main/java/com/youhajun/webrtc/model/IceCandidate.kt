package com.youhajun.webrtc.model

data class IceCandidate(
    val sdpMid: String?,
    val sdpMLineIndex: Int,
    val sdp: String,
) {
    fun toWebRtcCandidate(): org.webrtc.IceCandidate {
        return org.webrtc.IceCandidate(sdpMid, sdpMLineIndex, sdp)
    }
}