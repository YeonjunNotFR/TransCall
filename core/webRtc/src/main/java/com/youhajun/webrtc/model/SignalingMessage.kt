package com.youhajun.webrtc.model

sealed interface SignalingMessage {
    data class Offer(
        val sdp: String,
        val sessionId: String
    ) : SignalingMessage

    data class Answer(
        val sdp: String,
        val sessionId: String
    ) : SignalingMessage

    data class IceCandidate(
        val sdpMid: String,
        val sdpMLineIndex: Int,
        val candidate: String,
        val sessionId: String
    ) : SignalingMessage
}