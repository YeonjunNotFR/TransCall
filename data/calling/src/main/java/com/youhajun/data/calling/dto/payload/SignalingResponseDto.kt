package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.calling.payload.JoinedRoomPublisher
import com.youhajun.core.model.calling.payload.OnIceCandidate
import com.youhajun.core.model.calling.payload.OnNewPublisher
import com.youhajun.core.model.calling.payload.PublisherAnswer
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SubscriberOffer
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.data.calling.dto.PublisherFeedResponseDto
import com.youhajun.data.calling.dto.SubscriberFeedResponseDto
import com.youhajun.data.calling.dto.payload.JoinedRoomPublisherDto.Companion.JOINED_PUBLISHER_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.OnIceCandidateDto.Companion.ON_ICE_CANDIDATE_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.OnNewPublisherDto.Companion.ON_NEW_PUBLISHER_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.PublisherAnswerDto.Companion.PUBLISHER_ANSWER_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.SubscriberOfferDto.Companion.SUBSCRIBER_OFFER_ACTION_TYPE
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

internal sealed interface SignalingResponseDto : ResponsePayloadDto

@Serializable
@SerialName(JOINED_PUBLISHER_ACTION_TYPE)
internal data class JoinedRoomPublisherDto(
    @SerialName("publisherHandleId")
    val publisherHandleId: Long,
    @SerialName("feeds")
    val feeds: List<PublisherFeedResponseDto>,
    @SerialName("privateId")
    val privateId: Long? = null,
    @SerialName("mediaContentType")
    val mediaContentType: String,
) : SignalingResponseDto {

    companion object {
        internal const val JOINED_PUBLISHER_ACTION_TYPE: String = "joinedPublisher"
    }

    override fun toModel(): ResponsePayload = JoinedRoomPublisher(
        publisherHandleId = publisherHandleId,
        feeds = feeds.map { it.toModel() },
        privateId = privateId,
        mediaContentType = MediaContentType.fromType(mediaContentType)
    )
}

@Serializable
@SerialName(PUBLISHER_ANSWER_ACTION_TYPE)
internal data class PublisherAnswerDto(
    @SerialName("publisherHandleId")
    val publisherHandleId: Long,
    @SerialName("answerSdp")
    val answerSdp: String
) : SignalingResponseDto {

    companion object {
        internal const val PUBLISHER_ANSWER_ACTION_TYPE: String = "publisherAnswer"
    }

    override fun toModel(): ResponsePayload = PublisherAnswer(
        answerSdp = answerSdp,
        publisherHandleId = publisherHandleId
    )
}

@Serializable
@SerialName(SUBSCRIBER_OFFER_ACTION_TYPE)
internal data class SubscriberOfferDto(
    @SerialName("subscriberHandleId")
    val subscriberHandleId: Long,
    @SerialName("offerSdp")
    val offerSdp: String,
    @SerialName("feeds")
    val feeds: List<SubscriberFeedResponseDto>,
) : SignalingResponseDto {

    companion object {
        internal const val SUBSCRIBER_OFFER_ACTION_TYPE: String = "subscriberOffer"
    }

    override fun toModel(): ResponsePayload = SubscriberOffer(
        subscriberHandleId = subscriberHandleId,
        offerSdp = offerSdp,
        feeds = feeds.map { it.toModel() }
    )
}


@Serializable
@SerialName(ON_ICE_CANDIDATE_ACTION_TYPE)
internal data class OnIceCandidateDto(
    @SerialName("handleId")
    val handleId: Long,
    @SerialName("candidate")
    val candidate: String,
    @SerialName("sdpMid")
    val sdpMid: String?,
    @SerialName("sdpMLineIndex")
    val sdpMLineIndex: Int,
) : SignalingResponseDto {

    companion object {
        internal const val ON_ICE_CANDIDATE_ACTION_TYPE: String = "onIceCandidate"
    }

    override fun toModel(): ResponsePayload = OnIceCandidate(
        handleId = handleId,
        candidate = candidate,
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex
    )
}

@Serializable
@SerialName(ON_NEW_PUBLISHER_ACTION_TYPE)
internal data class OnNewPublisherDto(
    @SerialName("feeds")
    val feeds: List<PublisherFeedResponseDto>,
) : SignalingResponseDto {

    companion object {
        internal const val ON_NEW_PUBLISHER_ACTION_TYPE: String = "onNewPublisher"
    }

    override fun toModel(): OnNewPublisher = OnNewPublisher(
        feeds = feeds.map { it.toModel() }
    )
}