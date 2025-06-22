package com.youhajun.webrtc.session

import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.audio.AudioSessionManager
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.Config
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.LocalVideoEvent
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.RemoteAudioStream
import com.youhajun.webrtc.model.RemoteMediaUser
import com.youhajun.webrtc.model.RemoteVideoStream
import com.youhajun.webrtc.model.SignalingMessage
import com.youhajun.webrtc.model.TrackType
import com.youhajun.webrtc.peer.StreamPeerConnection
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import com.youhajun.webrtc.peer.StreamPeerType
import com.youhajun.webrtc.video.VideoSessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack
import java.util.UUID

class WebRtcSessionManagerImpl @AssistedInject constructor(
    @Assisted private val localUserId: String,
    @Assisted private val signalingClient: SignalingClient,
    private val peerConnectionFactory: StreamPeerConnectionFactory,
    audioFactory: AudioSessionManager.Factory,
    videoFactory: VideoSessionManager.Factory,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WebRtcSessionManager {

    private val sessionScope = CoroutineScope(SupervisorJob() + ioDispatcher)
    private val audioManager: AudioSessionManager = audioFactory.create(localUserId)
    private val videoManager: VideoSessionManager = videoFactory.create(localUserId)
    private val pendingIce = mutableListOf<IceCandidate>()
    private var offerArrived = CompletableDeferred<Unit>()
    private var currentSessionId: String? = null

    private val _mediaUsersFlow = combine(
        audioManager.audioStreamsFlow,
        videoManager.videoStreamsFlow
    ) { audioStreams, videoStreams -> combineMediaUsers(audioStreams, videoStreams) }
    override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = _mediaUsersFlow.stateIn(
        scope = sessionScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val mediaConstraints = MediaConstraints().apply {
        mandatory.addAll(
            listOf(
                MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
                MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"),
            ),
        )
    }

    private val peerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            coroutineScope = sessionScope,
            configuration = peerConnectionFactory.rtcConfig,
            type = StreamPeerType.SUBSCRIBER,
            mediaConstraints = mediaConstraints,
            onIceCandidateRequest = { iceCandidate, _ ->
                sendIceCandidate(iceCandidate)
            },
            onTrack = { rtpTransceiver ->
                rtpTransceiver?.receiver?.track()?.id()
                val track = rtpTransceiver?.receiver?.track() ?: return@makePeerConnection
                val (trackType, userId, type) = Config.getParsedTrackId(track.id())
                when(TrackType.fromString(trackType)) {
                    TrackType.VIDEO -> {
                        val videoTrack = track as VideoTrack
                        val remoteVideo = RemoteVideoStream(
                            userId = userId,
                            mediaContentType = MediaContentType.valueOf(type),
                            videoTrack = videoTrack,
                            isVideoEnable = videoTrack.enabled()
                        )
                        videoManager.addRemoteVideoTrack(remoteVideo)
                    }
                    TrackType.AUDIO -> {
                        val audioTrack = track as AudioTrack
                        val remoteAudio = RemoteAudioStream(
                            userId = userId,
                            mediaContentType = MediaContentType.valueOf(type),
                            audioTrack = audioTrack,
                            isMicEnabled = audioTrack.enabled(),
                        )
                        audioManager.addRemoteAudioTrack(remoteAudio)
                    }
                }
            },
        )
    }

    override fun start(isCaller: Boolean) {
        val streamIdList = listOf(Config.getStreamId(localUserId))
        val cameraTrack = videoManager.startCamera()
        val audioTrack = audioManager.startAudio()
        peerConnection.connection.addTrack(cameraTrack, streamIdList)
        peerConnection.connection.addTrack(audioTrack, streamIdList)

        signalingStart(isCaller)
    }

    override fun dispose() {
        videoManager.dispose()
        audioManager.dispose()
        sessionScope.cancel()
    }

    override fun flipCamera() = videoManager.flipCamera()
    override fun setCameraEnabled(enabled: Boolean) {
        sessionScope.launch {
            videoManager.setCameraEnabled(enabled)
        }
    }

    override fun selectAudioDevice(deviceType: AudioDeviceType) = audioManager.selectAudioDevice(deviceType)
    override fun setMicEnabled(enabled: Boolean) = audioManager.setMicEnabled(enabled)
    override fun setMuteEnable(enabled: Boolean) = audioManager.setMuteEnable(enabled)
    override fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean) =
        audioManager.setOutputEnable(userId, mediaContentType, enabled)

    private fun signalingStart(isCaller: Boolean) {
        sessionScope.launch {
            when {
                isCaller -> sendOffer()
                else -> {
                    val arrived = withTimeoutOrNull(10000) { offerArrived.await() }
                    if (arrived == null) sendOffer()
                }
            }
        }
    }

    private suspend fun sendOffer() {
        val sessionId = currentSessionId ?: makeSessionId()
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)
        val message = SignalingMessage.Offer(offer.description, sessionId)
        result.onSuccess {
            signalingClient.sendOffer(message)
        }
    }

    private suspend fun sendAnswer() {
        val sessionId = currentSessionId ?: return
        val answer = peerConnection.createAnswer().getOrThrow()
        val result = peerConnection.setLocalDescription(answer)
        val message = SignalingMessage.Answer(answer.description, sessionId)
        result.onSuccess {
            signalingClient.sendAnswer(message)
        }
    }

    private fun sendIceCandidate(iceCandidate: IceCandidate) {
        val sessionId = currentSessionId ?: return
        val message = SignalingMessage.IceCandidate(
            sdpMid = iceCandidate.sdpMid,
            sdpMLineIndex = iceCandidate.sdpMLineIndex,
            candidate = iceCandidate.sdp,
            sessionId = sessionId
        )
        signalingClient.sendIceCandidate(message)
    }

    private suspend fun handleOffer(sdp: String, sessionId: String) {
        currentSessionId = sessionId
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.OFFER, sdp)
        ).onSuccess {
            offerArrived.complete(Unit)
            pendingIce.forEach { peerConnection.addIceCandidate(it) }
            pendingIce.clear()
            sendAnswer()
        }
    }

    private suspend fun handleAnswer(sdp: String, sessionId: String) {
        if (currentSessionId != sessionId) return
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.ANSWER, sdp)
        )
    }

    private suspend fun handleIceCandidate(candidate: SignalingMessage.IceCandidate) {
        if (currentSessionId != candidate.sessionId) return
        val iceCandidate =
            IceCandidate(candidate.sdpMid, candidate.sdpMLineIndex, candidate.candidate)
        if (peerConnection.connection.remoteDescription != null) {
            peerConnection.addIceCandidate(iceCandidate)
        } else {
            pendingIce += iceCandidate
        }
    }

    private fun makeSessionId(): String {
        return UUID.randomUUID().toString().also {
            currentSessionId = it
        }
    }

    private fun restartNegotiation() {
        sessionScope.launch {
            peerConnection.connection.close()
            pendingIce.clear()
            offerArrived = CompletableDeferred()
            currentSessionId = null
            sendOffer()
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

    init {
        sessionScope.launch {
            signalingClient.observeSignalingMsg().collect {
                when (it) {
                    is SignalingMessage.Answer -> handleAnswer(it.sdp, it.sessionId)
                    is SignalingMessage.Offer -> handleOffer(it.sdp, it.sessionId)
                    is SignalingMessage.IceCandidate -> handleIceCandidate(it)
                }
            }
        }

        sessionScope.launch {
            signalingClient.observeMediaMsg().collect {
                when (it) {
                    is MediaMessage.AudioStateChange -> audioManager.setAudioStateChange(it)
                    is MediaMessage.VideoStateChange -> videoManager.setVideoStateChange(it)
                }
            }
        }

        sessionScope.launch {
            videoManager.localVideoEvent.collect {
                when(it) {
                    is LocalVideoEvent.EnabledChanged -> TODO()
                }
            }
        }
    }
}
