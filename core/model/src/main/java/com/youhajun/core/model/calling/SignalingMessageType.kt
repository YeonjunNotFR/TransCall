package com.youhajun.core.model.calling

sealed interface SignalingMessageType : CallingMessageType {
    data class Offer(
        val sdp: String,
        val sessionId: String,
    ) : SignalingMessageType

    data class Answer(
        val sdp: String,
        val sessionId: String,
    ) : SignalingMessageType

    data class Candidate(
        val candidate: String,
        val sdpMid: String,
        val sdpMLineIndex: Int,
        val sessionId: String,
    ) : SignalingMessageType
}
