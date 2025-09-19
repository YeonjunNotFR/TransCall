package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.calling.PublisherFeedResponse
import com.youhajun.core.model.calling.SubscriberFeedResponse
import com.youhajun.core.model.calling.type.MediaContentType

sealed interface SignalingResponse : ResponsePayload

data class JoinedRoomPublisher(
    val publisherHandleId: Long,
    val feeds: List<PublisherFeedResponse>,
    val privateId: Long?,
    val mediaContentType: MediaContentType
) : SignalingResponse

data class PublisherAnswer(
    val publisherHandleId: Long,
    val answerSdp: String
) : SignalingResponse

data class SubscriberOffer(
    val subscriberHandleId: Long,
    val offerSdp: String,
    val feeds: List<SubscriberFeedResponse>,
) : SignalingResponse

data class OnIceCandidate(
    val handleId: Long,
    val candidate: String,
    val sdpMid: String?,
    val sdpMLineIndex: Int
) : SignalingResponse

data class OnNewPublisher(
    val feeds: List<PublisherFeedResponse>,
) : SignalingResponse