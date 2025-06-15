package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.SignalingMessageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface SignalingDataDto: CallingDataDto {
    @Serializable
    @SerialName("offer")
    data class OfferDto(
        @SerialName("sdp")
        val sdp: String,
        @SerialName("sessionId")
        val sessionId: String,
    ) : SignalingDataDto

    @Serializable
    @SerialName("answer")
    data class AnswerDto(
        @SerialName("sdp")
        val sdp: String,
        @SerialName("sessionId")
        val sessionId: String,
    ) : SignalingDataDto

    @Serializable
    @SerialName("candidate")
    data class CandidateDto(
        @SerialName("candidate")
        val candidate: String,
        @SerialName("sdpMid")
        val sdpMid: String,
        @SerialName("sdpMLineIndex")
        val sdpMLineIndex: Int,
        @SerialName("sessionId")
        val sessionId: String,
    ) : SignalingDataDto

    override fun toModel(): SignalingMessageType = when (this) {
        is OfferDto -> SignalingMessageType.Offer(sdp, sessionId)
        is AnswerDto -> SignalingMessageType.Answer(sdp, sessionId)
        is CandidateDto -> SignalingMessageType.Candidate(
            candidate, sdpMid, sdpMLineIndex, sessionId
        )
    }
}

internal fun SignalingMessageType.toDto(): SignalingDataDto = when (this) {
    is SignalingMessageType.Offer -> SignalingDataDto.OfferDto(sdp, sessionId)
    is SignalingMessageType.Answer -> SignalingDataDto.AnswerDto(sdp, sessionId)
    is SignalingMessageType.Candidate -> SignalingDataDto.CandidateDto(candidate, sdpMid, sdpMLineIndex, sessionId)
}