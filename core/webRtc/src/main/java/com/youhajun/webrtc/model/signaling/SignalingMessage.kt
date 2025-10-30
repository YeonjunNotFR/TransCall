package com.youhajun.webrtc.model.signaling

sealed interface SignalingMessage

sealed interface SignalingMessageRequest : SignalingMessage
sealed interface SignalingMessageResponse : SignalingMessage

data class JoinRoomPublisher(
    val handleId: Long,
    val mediaContentType: String,
) : SignalingMessageRequest

data class PublisherOffer(
    val offerSdp: String,
    val handleId: Long,
    val mediaContentType: String,
    val audioCodec: String?,
    val videoCodec: String?,
    val videoMid: String?,
    val audioMid: String?,
) : SignalingMessageRequest

data class JoinRoomSubscriber(
    val privateId: Long?,
    val feeds: List<SubscriberFeedRequest>,
) : SignalingMessageRequest

data class SubscriberUpdate(
    val subscribeFeeds: List<SubscriberFeedRequest>,
    val unsubscribeFeeds: List<SubscriberFeedRequest>,
) : SignalingMessageRequest

data class SubscriberAnswer(
    val answerSdp: String,
    val handleId: Long
) : SignalingMessageRequest

data class SignalingIceCandidate(
    val handleId: Long,
    val candidate: String,
    val sdpMid: String?,
    val sdpMLineIndex: Int,
) : SignalingMessageRequest

data class CompleteIceCandidate(
    val handleId: Long
) : SignalingMessageRequest

data class JoinedRoomPublisher(
    val publisherHandleId: Long,
    val feeds: List<PublisherFeedResponse>,
    val privateId: Long?,
    val mediaContentType: String
) : SignalingMessageResponse

data class PublisherAnswer(
    val publisherHandleId: Long,
    val answerSdp: String,
) : SignalingMessageResponse

data class SubscriberOffer(
    val subscriberHandleId: Long,
    val offerSdp: String,
    val feeds: List<SubscriberFeedResponse>
) : SignalingMessageResponse

data class OnIceCandidate(
    val handleId: Long,
    val candidate: String,
    val sdpMid: String?,
    val sdpMLineIndex: Int
) : SignalingMessageResponse

data class OnNewPublisher(
    val feeds: List<PublisherFeedResponse>,
) : SignalingMessageResponse