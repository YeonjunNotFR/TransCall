package com.youhajun.webrtc.session

import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.audio.AudioSessionManager
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.CameraEnabledChanged
import com.youhajun.webrtc.model.CompleteIceCandidate
import com.youhajun.webrtc.model.IceCandidate
import com.youhajun.webrtc.model.JoinRoomPublisher
import com.youhajun.webrtc.model.JoinRoomSubscriber
import com.youhajun.webrtc.model.JoinedRoomPublisher
import com.youhajun.webrtc.model.LocalAudioEvent
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.LocalVideoEvent
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaStateChanged
import com.youhajun.webrtc.model.MediaStateInit
import com.youhajun.webrtc.model.MicEnableChanged
import com.youhajun.webrtc.model.OnIceCandidate
import com.youhajun.webrtc.model.OnNewPublisher
import com.youhajun.webrtc.model.PublisherAnswer
import com.youhajun.webrtc.model.PublisherFeedResponse
import com.youhajun.webrtc.model.PublisherOffer
import com.youhajun.webrtc.model.RemoteAudioStream
import com.youhajun.webrtc.model.RemoteMediaUser
import com.youhajun.webrtc.model.RemoteVideoStream
import com.youhajun.webrtc.model.SignalingIceCandidate
import com.youhajun.webrtc.model.SubscriberAnswer
import com.youhajun.webrtc.model.SubscriberMidMapper
import com.youhajun.webrtc.model.SubscriberOffer
import com.youhajun.webrtc.model.SubscriberUpdate
import com.youhajun.webrtc.model.TrackType
import com.youhajun.webrtc.model.TurnCredential
import com.youhajun.webrtc.model.VideoRoomHandleInfo
import com.youhajun.webrtc.model.toMidMap
import com.youhajun.webrtc.peer.StreamPeerConnection
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import com.youhajun.webrtc.peer.StreamPeerType
import com.youhajun.webrtc.video.VideoSessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.MediaConstraints
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack
import javax.inject.Inject

internal class WebRtcSessionManagerImpl @Inject constructor(
    private val peerConnectionFactory: StreamPeerConnectionFactory,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val audioManager: AudioSessionManager,
    private val videoManager: VideoSessionManager,
) : WebRtcSessionManager {

    private lateinit var localUserId: String
    private lateinit var signalingClient: SignalingClient
    private val sessionScope = CoroutineScope(SupervisorJob() + ioDispatcher)
    private var privateId: Long? = null
    private val midMapper: MutableMap<String, SubscriberMidMapper> = mutableMapOf()

    private val _mediaUsersFlow = combine(
        audioManager.audioStreamsFlow,
        videoManager.videoStreamsFlow
    ) { audioStreams, videoStreams -> combineMediaUsers(audioStreams, videoStreams) }

    override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = _mediaUsersFlow.stateIn(
        scope = sessionScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    override val micByteFlow: SharedFlow<ByteArray> = audioManager.myAudioPcmFlow

    private val mediaConstraints = MediaConstraints().apply {
        mandatory.addAll(
            listOf(
                MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
                MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"),
            ),
        )
    }

    private val publisherPeerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            coroutineScope = sessionScope,
            type = StreamPeerType.PUBLISHER,
            mediaConstraints = mediaConstraints,
            onIceCandidateRequest = { iceCandidate, handleId ->
                if(iceCandidate == null) sendCompleteIceCandidate(handleId)
                else sendIceCandidate(handleId, iceCandidate.toWebRtcCandidate())
            },
        )
    }

    private val subscriberPeerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            coroutineScope = sessionScope,
            type = StreamPeerType.SUBSCRIBER,
            mediaConstraints = mediaConstraints,
            onTrack = { rtpTransceiver ->
                val track = rtpTransceiver?.receiver?.track() ?: return@makePeerConnection
                val mapper = midMapper[rtpTransceiver.mid] ?: return@makePeerConnection
                when (mapper.trackType) {
                    TrackType.VIDEO -> {
                        val videoTrack = track as VideoTrack
                        val remoteVideo = RemoteVideoStream(
                            userId = mapper.userId,
                            mediaContentType = mapper.mediaContentType.type,
                            videoTrack = videoTrack,
                            isVideoEnable = videoTrack.enabled()
                        )
                        videoManager.addRemoteVideoTrack(remoteVideo)
                    }

                    TrackType.AUDIO -> {
                        val audioTrack = track as AudioTrack
                        val remoteAudio = RemoteAudioStream(
                            userId = mapper.userId,
                            mediaContentType = mapper.mediaContentType.type,
                            audioTrack = audioTrack,
                            isMicEnabled = audioTrack.enabled(),
                        )
                        audioManager.addRemoteAudioTrack(remoteAudio)
                    }
                }
            },
            onIceCandidateRequest = { iceCandidate, handleId ->
                if(iceCandidate == null) sendCompleteIceCandidate(handleId)
                else sendIceCandidate(handleId, iceCandidate.toWebRtcCandidate())
            },
        )
    }

    private val peerConnectionList: List<StreamPeerConnection> by lazy {
        listOf(publisherPeerConnection, subscriberPeerConnection)
    }

    override fun initConfig(
        localUserId: String,
        turnCredential: TurnCredential,
        signalingClient: SignalingClient
    ) {
        this.localUserId = localUserId
        this.signalingClient = signalingClient
        peerConnectionFactory.initRtcConfig(turnCredential)
        collectSignalingResponse()
        collectMediaStateResponse()
        collectLocalAudioEvent()
        collectLocalVideoEvent()
    }

    override fun start(videoRoomHandleInfo: VideoRoomHandleInfo) {
        setVideoRoomHandleInfo(videoRoomHandleInfo)
        val cameraTrack = videoManager.startCamera(localUserId)
        val audioTrack = audioManager.startAudio(localUserId)
        publisherPeerConnection.connection.addTrack(cameraTrack)
        publisherPeerConnection.connection.addTrack(audioTrack)

        sessionScope.launch {
            sendJoinPublisher(publisherPeerConnection.handleId, MediaContentType.DEFAULT)
        }
    }

    override fun dispose() {
        videoManager.dispose()
        audioManager.dispose()
        runCatching { publisherPeerConnection.connection.dispose() }
        sessionScope.cancel()
    }

    override fun flipCamera() = videoManager.flipCamera(localUserId)

    override fun setCameraEnabled(enabled: Boolean) {
        videoManager.setCameraEnabled(localUserId, enabled)
    }

    override fun selectAudioDevice(deviceType: AudioDeviceType) = audioManager.selectAudioDevice(deviceType)

    override fun setMicEnabled(enabled: Boolean) = audioManager.setMicEnabled(localUserId, enabled)

    override fun setMuteEnable(enabled: Boolean) = audioManager.setMuteEnable(enabled)

    override fun setOutputEnable(userId: String, mediaContentType: String, enabled: Boolean) =
        audioManager.setOutputEnable(userId, MediaContentType.fromType(mediaContentType), enabled)

    private fun setVideoRoomHandleInfo(videoRoomHandleInfo: VideoRoomHandleInfo) {
        publisherPeerConnection.setHandleId(videoRoomHandleInfo.defaultPublisherHandleId)
        subscriberPeerConnection.setHandleId(videoRoomHandleInfo.subscriberHandleId)
    }

    private fun sendIceCandidate(handleId: Long, iceCandidate: IceCandidate) {
        val message = SignalingIceCandidate(
            sdpMid = iceCandidate.sdpMid,
            sdpMLineIndex = iceCandidate.sdpMLineIndex,
            candidate = iceCandidate.sdp,
            handleId = handleId
        )
        sessionScope.launch {
            signalingClient.sendSignalingRequest(message)
        }
    }

    private fun sendCompleteIceCandidate(handleId: Long) {
        val message = CompleteIceCandidate(handleId = handleId)
        sessionScope.launch {
            signalingClient.sendSignalingRequest(message)
        }
    }

    private fun combineMediaUsers(
        audios: List<CallAudioStream>,
        videos: List<CallVideoStream>
    ): List<CallMediaUser> {
        val videoMap = videos.associateBy { it.key }
        val audioMap = audios.associateBy { it.key }
        val commonKeys = videoMap.keys.intersect(audioMap.keys)

        return commonKeys.mapNotNull { key ->
            val audio = audioMap[key] ?: return@mapNotNull null
            val video = videoMap[key] ?: return@mapNotNull null

            when {
                audio is LocalAudioStream && video is LocalVideoStream -> LocalMediaUser(
                    userId = audio.userId,
                    mediaContentType = audio.mediaContentType,
                    videoStream = video,
                    audioStream = audio
                )

                audio is RemoteAudioStream && video is RemoteVideoStream -> RemoteMediaUser(
                    userId = audio.userId,
                    mediaContentType = audio.mediaContentType,
                    videoStream = video,
                    audioStream = audio
                )

                else -> null
            }
        }
    }

    private suspend fun sendJoinPublisher(handleId: Long, mediaContentType: MediaContentType) {
        val message = JoinRoomPublisher(handleId = handleId, mediaContentType = mediaContentType.type)
        signalingClient.sendSignalingRequest(message)
    }

    private suspend fun sendJoinSubscriber(feeds: List<PublisherFeedResponse>) {
        if(feeds.isEmpty()) return

        val message = JoinRoomSubscriber(
            privateId = privateId,
            feeds = feeds.map { it.toSubscriberFeedRequest() }
        )
        signalingClient.sendSignalingRequest(message)
    }

    private suspend fun sendSubscribeAdd(feeds: List<PublisherFeedResponse>) {
        if (feeds.isEmpty()) return
        val message = SubscriberUpdate(
            subscribeFeeds = feeds.map { it.toSubscriberFeedRequest() },
            unsubscribeFeeds = emptyList()
        )
        signalingClient.sendSignalingRequest(message)
    }

    private suspend fun sendPublisherOffer(handleId: Long, mediaContentType: String) {
        val peerConnection = peerConnectionList.first { it.handleId == handleId }
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)

        val videoCodec = peerConnectionFactory.getVideoCodec()
        val audioCodec = peerConnectionFactory.getAudioCodec()
        val message = PublisherOffer(
            offerSdp = offer.description,
            handleId = handleId,
            audioCodec = audioCodec,
            videoCodec = videoCodec,
            audioMid = peerConnection.audioMid,
            videoMid = peerConnection.videoMid,
            mediaContentType = mediaContentType
        )

        result.onSuccess {
            signalingClient.sendSignalingRequest(message)
        }
    }

    private suspend fun onPublisherAnswer(handleId: Long, answerSdp: String) {
        peerConnectionList
            .first { it.handleId == handleId }
            .setRemoteDescription(SessionDescription(SessionDescription.Type.ANSWER, answerSdp)
        )
    }

    private suspend fun onSubscriberOffer(offerSdp: String, handleId: Long) {
        subscriberPeerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.OFFER, offerSdp)
        ).onSuccess {
            val answer = subscriberPeerConnection.createAnswer().getOrThrow()
            val result = subscriberPeerConnection.setLocalDescription(answer)
            val message = SubscriberAnswer(answerSdp = answer.description, handleId = handleId)

            result.onSuccess {
                signalingClient.sendSignalingRequest(message)
            }
        }
    }

    private suspend fun onRemoteIceCandidate(handleId: Long, iceCandidate: IceCandidate) {
        peerConnectionList
            .first { it.handleId == handleId }
            .addIceCandidate(iceCandidate.toWebRtcCandidate())
    }

    private fun collectMediaStateResponse() = sessionScope.launch {
        signalingClient.observeMediaStateResponse().collect {
            when(it) {
                is MediaStateChanged -> {
                    audioManager.onMediaStateChanged(it.mediaState)
                    videoManager.onMediaStateChanged(it.mediaState)
                }
                is MediaStateInit -> {
                    audioManager.onMediaStateInit(it.mediaStateList)
                    videoManager.onMediaStateInit(it.mediaStateList)
                }
            }
        }
    }

    private fun collectSignalingResponse() = sessionScope.launch {
        signalingClient.observeSignalingResponse().collect {
            when(it) {
                is JoinedRoomPublisher -> {
                    sendPublisherOffer(it.publisherHandleId, it.mediaContentType)
                    if(it.publisherHandleId == publisherPeerConnection.handleId) {
                        privateId = it.privateId
                        sendJoinSubscriber(it.feeds)
                    }
                }
                is PublisherAnswer -> onPublisherAnswer(it.publisherHandleId, it.answerSdp)
                is SubscriberOffer -> {
                    midMapper.putAll(it.feeds.toMidMap())
                    onSubscriberOffer(it.offerSdp, it.subscriberHandleId)
                }
                is OnIceCandidate -> onRemoteIceCandidate(
                    handleId = it.handleId,
                    iceCandidate = IceCandidate(
                        sdpMid = it.sdpMid,
                        sdpMLineIndex = it.sdpMLineIndex,
                        sdp = it.candidate
                    )
                )

                is OnNewPublisher -> {
                    if(midMapper.isEmpty()) {
                        sendJoinSubscriber(it.feeds)
                    } else {
                        sendSubscribeAdd(it.feeds)
                    }
                }
            }
        }
    }

    private fun collectLocalVideoEvent() {
        sessionScope.launch {
            videoManager.localVideoEvent.collect {
                val message = when (it) {
                    is LocalVideoEvent.CameraEnabledChanged -> CameraEnabledChanged(it.enabled)
                }
                signalingClient.sendMediaStateRequest(message)
            }
        }
    }

    private fun collectLocalAudioEvent() {
        sessionScope.launch {
            audioManager.localAudioEvent.collect {
                val message = when (it) {
                    is LocalAudioEvent.MicEnableChanged -> MicEnableChanged(it.enabled)
                }
                signalingClient.sendMediaStateRequest(message)
            }
        }
    }

    private fun org.webrtc.IceCandidate.toWebRtcCandidate(): IceCandidate = IceCandidate(
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex,
        sdp = sdp
    )
}
