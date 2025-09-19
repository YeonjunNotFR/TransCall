package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.calling.SubscriberFeedRequest
import com.youhajun.core.model.calling.type.MediaContentType

sealed interface SignalingRequest : RequestPayload

data class JoinRoomPublisher(
    val handleId: Long,
    val mediaContentType: MediaContentType,
) : SignalingRequest

data class PublisherOffer(
    val offerSdp: String,
    val handleId: Long,
    val mediaContentType: MediaContentType,
    val audioCodec: String?,
    val videoCodec: String?,
    val videoMid: String?,
    val audioMid: String?,
) : SignalingRequest

data class JoinRoomSubscriber(
    val privateId: Long?,
    val feeds: List<SubscriberFeedRequest>,
) : SignalingRequest

data class SubscriberAnswer(
    val answerSdp: String,
    val handleId: Long
) : SignalingRequest

data class CompleteIceCandidate(
    val handleId: Long
) : SignalingRequest

data class SubscriberUpdate(
    val subscribeFeeds: List<SubscriberFeedRequest>,
    val unsubscribeFeeds: List<SubscriberFeedRequest>,
) : SignalingRequest

data class SignalingIceCandidate(
    val handleId: Long,
    val candidate: String,
    val sdpMid: String?,
    val sdpMLineIndex: Int,
) : SignalingRequest