package com.youhajun.data.common.dto.calling.payload

import com.youhajun.core.model.calling.payload.CompleteIceCandidate
import com.youhajun.core.model.calling.payload.JoinRoomPublisher
import com.youhajun.core.model.calling.payload.JoinRoomSubscriber
import com.youhajun.core.model.calling.payload.PublisherOffer
import com.youhajun.core.model.calling.payload.SignalingIceCandidate
import com.youhajun.core.model.calling.payload.SignalingRequest
import com.youhajun.core.model.calling.payload.SubscriberAnswer
import com.youhajun.core.model.calling.payload.SubscriberUpdate
import com.youhajun.data.common.dto.calling.SubscriberFeedRequestDto
import com.youhajun.data.common.dto.calling.payload.CompleteIceCandidateDto.Companion.COMPLETE_ICE_CANDIDATE_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.JoinRoomPublisherDto.Companion.JOIN_PUBLISHER_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.JoinRoomSubscriberDto.Companion.JOIN_SUBSCRIBER_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.PublisherOfferDto.Companion.PUBLISHER_OFFER_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.SignalingIceCandidateDto.Companion.ICE_CANDIDATE_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.SubscriberAnswerDto.Companion.SUBSCRIBER_ANSWER_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.SubscriberUpdateDto.Companion.SUBSCRIBER_UPDATE_ACTION_TYPE
import com.youhajun.data.common.dto.calling.toDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface SignalingRequestDto : RequestPayloadDto

@Serializable
@SerialName(JOIN_PUBLISHER_ACTION_TYPE)
data class JoinRoomPublisherDto(
    @SerialName("mediaContentType")
    val mediaContentType: String,
    @SerialName("handleId")
    val handleId: Long,
) : SignalingRequestDto {
    companion object {
        const val JOIN_PUBLISHER_ACTION_TYPE: String = "joinPublisher"
    }
}

@Serializable
@SerialName(PUBLISHER_OFFER_ACTION_TYPE)
data class PublisherOfferDto(
    @SerialName("offerSdp")
    val offerSdp: String,
    @SerialName("handleId")
    val handleId: Long,
    @SerialName("mediaContentType")
    val mediaContentType: String,
    @SerialName("audioCodec")
    val audioCodec: String?,
    @SerialName("videoCodec")
    val videoCodec: String?,
    @SerialName("videoMid")
    val videoMid: String?,
    @SerialName("audioMid")
    val audioMid: String?,
) : SignalingRequestDto {
    companion object {
        const val PUBLISHER_OFFER_ACTION_TYPE: String = "publisherOffer"
    }
}

@Serializable
@SerialName(JOIN_SUBSCRIBER_ACTION_TYPE)
data class JoinRoomSubscriberDto(
    @SerialName("privateId")
    val privateId: Long?,
    @SerialName("feeds")
    val feeds: List<SubscriberFeedRequestDto>,
) : SignalingRequestDto {
    companion object {
        const val JOIN_SUBSCRIBER_ACTION_TYPE: String = "joinSubscriber"
    }
}

@Serializable
@SerialName(SUBSCRIBER_ANSWER_ACTION_TYPE)
data class SubscriberAnswerDto(
    @SerialName("answerSdp")
    val answerSdp: String,
    @SerialName("handleId")
    val handleId: Long
) : SignalingRequestDto {
    companion object {
        const val SUBSCRIBER_ANSWER_ACTION_TYPE: String = "subscriberAnswer"
    }
}

@Serializable
@SerialName(SUBSCRIBER_UPDATE_ACTION_TYPE)
data class SubscriberUpdateDto(
    @SerialName("subscribeFeeds")
    val subscribeFeeds: List<SubscriberFeedRequestDto>,
    @SerialName("unsubscribeFeeds")
    val unsubscribeFeeds: List<SubscriberFeedRequestDto>,
) : SignalingRequestDto {
    companion object {
        const val SUBSCRIBER_UPDATE_ACTION_TYPE: String = "subscriberUpdate"
    }
}

@Serializable
@SerialName(ICE_CANDIDATE_ACTION_TYPE)
data class SignalingIceCandidateDto(
    @SerialName("handleId")
    val handleId: Long,
    @SerialName("candidate")
    val candidate: String,
    @SerialName("sdpMid")
    val sdpMid: String?,
    @SerialName("sdpMLineIndex")
    val sdpMLineIndex: Int,
) : SignalingRequestDto {
    companion object {
        const val ICE_CANDIDATE_ACTION_TYPE: String = "iceCandidate"
    }
}

@Serializable
@SerialName(COMPLETE_ICE_CANDIDATE_ACTION_TYPE)
data class CompleteIceCandidateDto(
    @SerialName("handleId")
    val handleId: Long,
) : SignalingRequestDto {
    companion object {
        const val COMPLETE_ICE_CANDIDATE_ACTION_TYPE: String = "completeIceCandidate"
    }
}

fun SignalingRequest.toDto(): SignalingRequestDto = when (this) {
    is JoinRoomPublisher -> JoinRoomPublisherDto(
        handleId = handleId,
        mediaContentType = mediaContentType.type
    )

    is PublisherOffer -> PublisherOfferDto(
        offerSdp = offerSdp,
        handleId = handleId,
        mediaContentType = mediaContentType.type,
        audioCodec = audioCodec,
        videoCodec = videoCodec,
        videoMid = videoMid,
        audioMid = audioMid
    )

    is JoinRoomSubscriber -> JoinRoomSubscriberDto(
        privateId = privateId,
        feeds = feeds.map { it.toDto() }
    )

    is SubscriberAnswer -> SubscriberAnswerDto(
        answerSdp = answerSdp,
        handleId = handleId
    )

    is SignalingIceCandidate -> SignalingIceCandidateDto(
        handleId = handleId,
        candidate = candidate,
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex
    )

    is CompleteIceCandidate -> CompleteIceCandidateDto(
        handleId = handleId
    )

    is SubscriberUpdate -> SubscriberUpdateDto(
        subscribeFeeds = subscribeFeeds.map { it.toDto() },
        unsubscribeFeeds = unsubscribeFeeds.map { it.toDto() }
    )
}