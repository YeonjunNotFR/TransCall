package com.youhajun.feature.call.service

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.payload.ChangedRoom
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.calling.payload.MediaStateRequest
import com.youhajun.core.model.calling.payload.MediaStateResponse
import com.youhajun.core.model.calling.payload.SignalingRequest
import com.youhajun.core.model.calling.payload.SignalingResponse
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.calling.type.MessageType
import com.youhajun.core.model.room.RoomStatus
import com.youhajun.core.model.user.MyInfo
import com.youhajun.domain.calling.usecase.CallCloseUseCase
import com.youhajun.domain.calling.usecase.CallConnectUseCase
import com.youhajun.domain.calling.usecase.CallingSendBinaryUseCase
import com.youhajun.domain.calling.usecase.CallingSendUseCase
import com.youhajun.domain.calling.usecase.GetTurnCredentialUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.call.api.service.CallServiceContract
import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.CameraEnabledChanged
import com.youhajun.webrtc.model.CompleteIceCandidate
import com.youhajun.webrtc.model.JoinRoomPublisher
import com.youhajun.webrtc.model.JoinRoomSubscriber
import com.youhajun.webrtc.model.JoinedRoomPublisher
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.MediaState
import com.youhajun.webrtc.model.MediaStateChanged
import com.youhajun.webrtc.model.MediaStateInit
import com.youhajun.webrtc.model.MediaStateMessageRequest
import com.youhajun.webrtc.model.MediaStateMessageResponse
import com.youhajun.webrtc.model.MicEnableChanged
import com.youhajun.webrtc.model.OnIceCandidate
import com.youhajun.webrtc.model.OnNewPublisher
import com.youhajun.webrtc.model.PublisherAnswer
import com.youhajun.webrtc.model.PublisherFeedResponse
import com.youhajun.webrtc.model.PublisherFeedResponseStream
import com.youhajun.webrtc.model.PublisherOffer
import com.youhajun.webrtc.model.SignalingIceCandidate
import com.youhajun.webrtc.model.SignalingMessageRequest
import com.youhajun.webrtc.model.SignalingMessageResponse
import com.youhajun.webrtc.model.SubscriberAnswer
import com.youhajun.webrtc.model.SubscriberFeedRequest
import com.youhajun.webrtc.model.SubscriberFeedResponse
import com.youhajun.webrtc.model.SubscriberOffer
import com.youhajun.webrtc.model.SubscriberUpdate
import com.youhajun.webrtc.model.TurnCredential
import com.youhajun.webrtc.model.VideoRoomHandleInfo
import com.youhajun.webrtc.session.WebRtcSessionManager
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.youhajun.core.model.calling.PublisherFeedResponse as DomainPublisherFeedResponse
import com.youhajun.core.model.calling.PublisherFeedResponseStream as DomainPublisherFeedResponseStream
import com.youhajun.core.model.calling.SubscriberFeedRequest as DomainSubscriberFeedRequest
import com.youhajun.core.model.calling.SubscriberFeedResponse as DomainSubscriberFeedResponse
import com.youhajun.core.model.calling.TurnCredential as DomainTurnCredential
import com.youhajun.core.model.calling.VideoRoomHandleInfo as DomainVideoRoomHandleInfo
import com.youhajun.core.model.calling.payload.CameraEnableChanged as DomainCameraEnableChanged
import com.youhajun.core.model.calling.payload.CompleteIceCandidate as DomainCompleteIceCandidate
import com.youhajun.core.model.calling.payload.JoinRoomPublisher as DomainJoinRoomPublisher
import com.youhajun.core.model.calling.payload.JoinRoomSubscriber as DomainJoinRoomSubscriber
import com.youhajun.core.model.calling.payload.JoinedRoomPublisher as DomainJoinedRoomPublisher
import com.youhajun.core.model.calling.payload.MediaState as DomainMediaState
import com.youhajun.core.model.calling.payload.MediaStateChanged as DomainMediaStateChanged
import com.youhajun.core.model.calling.payload.MediaStateInit as DomainMediaStateInit
import com.youhajun.core.model.calling.payload.MicEnableChanged as DomainMicEnableChanged
import com.youhajun.core.model.calling.payload.OnIceCandidate as DomainOnIceCandidate
import com.youhajun.core.model.calling.payload.OnNewPublisher as DomainOnNewPublisher
import com.youhajun.core.model.calling.payload.PublisherAnswer as DomainPublisherAnswer
import com.youhajun.core.model.calling.payload.PublisherOffer as DomainPublisherOffer
import com.youhajun.core.model.calling.payload.SignalingIceCandidate as DomainIceCandidate
import com.youhajun.core.model.calling.payload.SubscriberAnswer as DomainSubscriberAnswer
import com.youhajun.core.model.calling.payload.SubscriberOffer as DomainSubscriberOffer
import com.youhajun.core.model.calling.payload.SubscriberUpdate as DomainSubscriberUpdate

@ServiceScoped
class CallServiceManager @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val webRtcManager: WebRtcSessionManager,
    private val callConnectUseCase: CallConnectUseCase,
    private val callCloseUseCase: CallCloseUseCase,
    private val callingSendUseCase: CallingSendUseCase,
    private val callingSendBinaryUseCase: CallingSendBinaryUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getTurnCredentialUseCase: GetTurnCredentialUseCase,
) : CallServiceContract {

    interface ServiceCallback {
        fun onError(error: Throwable)
        fun onLeave()
        fun onMediaStateChanged(isMicEnable: Boolean, isCameraEnable: Boolean)
    }

    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)
    private var serviceCallback: ServiceCallback? = null

    override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = webRtcManager.mediaUsersFlow
    private val _messageFlow = MutableSharedFlow<ServerMessage>(replay = 1, extraBufferCapacity = 50)
    override val messageFlow: SharedFlow<ServerMessage> = _messageFlow.asSharedFlow()

    private val webRtcSignalingClient by lazy {
        object : SignalingClient {
            override suspend fun sendSignalingRequest(request: SignalingMessageRequest) {
                val msg = ClientMessage(type = MessageType.SIGNALING, payload = request.toDomain())
                callingSendUseCase(msg)
            }

            override suspend fun sendMediaStateRequest(request: MediaStateMessageRequest) {
                val msg = ClientMessage(type = MessageType.MEDIA_STATE, payload = request.toDomain())
                callingSendUseCase(msg)
            }

            override fun observeSignalingResponse(): Flow<SignalingMessageResponse> =
                messageFlow.mapNotNull { (it.payload as? SignalingResponse)?.toWebRtc() }

            override fun observeMediaStateResponse(): Flow<MediaStateMessageResponse> =
                messageFlow.mapNotNull { (it.payload as? MediaStateResponse)?.toWebRtc() }
        }
    }

    private var currentUser: MyInfo? = null
    private var currentRoomId: String? = null
    private var roomStatus: RoomStatus = RoomStatus.WAITING

    override fun leaveCall() {
        serviceCallback?.onLeave()
    }

    override fun flipCamera() = webRtcManager.flipCamera()

    override fun setCameraEnabled(enabled: Boolean) {
        webRtcManager.setCameraEnabled(enabled)
    }

    override fun setMicEnabled(enabled: Boolean) {
        webRtcManager.setMicEnabled(enabled)
    }

    override fun setMuteEnable(enabled: Boolean) {
        webRtcManager.setMuteEnable(enabled)
    }

    override fun setOutputEnable(
        userId: String,
        mediaContentType: MediaContentType,
        enabled: Boolean
    ) {
        webRtcManager.setOutputEnable(userId, mediaContentType.type, enabled)
    }

    override fun setAudioDeviceChange(deviceType: AudioDeviceType) {
        webRtcManager.selectAudioDevice(deviceType)
    }

    fun initManager(callback: ServiceCallback) {
        this.serviceCallback = callback
    }

    fun startCall(roomId: String) {
        scope.launch {
            runCatching {
                currentRoomId = roomId
                val user = getMyInfoUseCase().getOrThrow().also { currentUser = it }
                val credential = getTurnCredentialUseCase().getOrThrow()

                webRtcManager.initConfig(user.userId, credential.toWebRtc(), webRtcSignalingClient)
                collectConnectRoom(roomId)
                collectLocalMedia()
            }.onFailure { error ->
                Timber.e(error, "startCall error")
                serviceCallback?.onError(error)
            }
        }
    }

    fun disposeAll() {
        scope.launch {
            runCatching { callCloseUseCase() }
            webRtcManager.dispose()
            scope.cancel()
            serviceCallback = null
        }
    }

    private fun collectMicChunk() {
        scope.launch {
            webRtcManager.micByteFlow.collect {
                if (roomStatus == RoomStatus.IN_PROGRESS) callingSendBinaryUseCase(it)
            }
        }
    }

    private fun collectConnectRoom(roomId: String) {
        scope.launch {
            callConnectUseCase(roomId).collect {
                handleServerMessage(it)
            }
        }
    }

    private fun collectLocalMedia() {
        scope.launch {
            mediaUsersFlow
                .mapNotNull {
                    it.firstOrNull { it is LocalMediaUser && it.mediaContentType == MediaContentType.DEFAULT.type }
                }
                .distinctUntilChanged { old, new ->
                    old.audioStream.isMicEnabled == new.audioStream.isMicEnabled && old.videoStream.isVideoEnable == new.videoStream.isVideoEnable
                }
                .collect {
                    serviceCallback?.onMediaStateChanged(
                        isMicEnable = it.audioStream.isMicEnabled,
                        isCameraEnable = it.videoStream.isVideoEnable
                    )
                }
        }
    }

    private suspend fun handleServerMessage(message: ServerMessage) {
        _messageFlow.emit(message)
        when (val payload = message.payload) {
            is ConnectedRoom -> {
                roomStatus = payload.roomInfo.status
                webRtcManager.start(payload.videoRoomHandleInfo.toWebRtc())
            }

            is ChangedRoom -> {
                roomStatus = payload.roomInfo.status
            }

            is SttStart -> collectMicChunk()

            else -> Unit
        }
    }
}

private fun SignalingResponse.toWebRtc(): SignalingMessageResponse {
    return when (this) {
        is DomainJoinedRoomPublisher -> JoinedRoomPublisher(
            publisherHandleId = publisherHandleId,
            feeds = feeds.map { it.toWebRtc() },
            privateId = privateId,
            mediaContentType = mediaContentType.type
        )

        is DomainPublisherAnswer -> PublisherAnswer(
            publisherHandleId = publisherHandleId,
            answerSdp = answerSdp
        )

        is DomainSubscriberOffer -> SubscriberOffer(
            subscriberHandleId = subscriberHandleId,
            offerSdp = offerSdp,
            feeds = feeds.map { it.toWebRtc() }
        )

        is DomainOnIceCandidate -> OnIceCandidate(
            handleId = handleId,
            candidate = candidate,
            sdpMid = sdpMid,
            sdpMLineIndex = sdpMLineIndex
        )

        is DomainOnNewPublisher -> OnNewPublisher(
            feeds = feeds.map { it.toWebRtc() }
        )
    }
}

private fun SignalingMessageRequest.toDomain(): SignalingRequest = when (this) {
    is JoinRoomPublisher -> DomainJoinRoomPublisher(
        handleId = handleId,
        mediaContentType = MediaContentType.fromType(mediaContentType)
    )

    is JoinRoomSubscriber -> DomainJoinRoomSubscriber(
        privateId = privateId,
        feeds = feeds.map { it.toDomain() }
    )

    is PublisherOffer -> DomainPublisherOffer(
        offerSdp = offerSdp,
        handleId = handleId,
        MediaContentType.fromType(mediaContentType),
        audioCodec = audioCodec,
        videoCodec = videoCodec,
        videoMid = videoMid,
        audioMid = audioMid
    )

    is SignalingIceCandidate -> DomainIceCandidate(
        handleId = handleId,
        candidate = candidate,
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex
    )

    is SubscriberAnswer -> DomainSubscriberAnswer(
        answerSdp = answerSdp,
        handleId = handleId
    )

    is CompleteIceCandidate -> DomainCompleteIceCandidate(
        handleId = handleId
    )

    is SubscriberUpdate -> DomainSubscriberUpdate(
        subscribeFeeds = subscribeFeeds.map { it.toDomain() },
        unsubscribeFeeds = unsubscribeFeeds.map { it.toDomain() }
    )
}

private fun DomainVideoRoomHandleInfo.toWebRtc() = VideoRoomHandleInfo(
    defaultPublisherHandleId = defaultPublisherHandleId,
    screenSharePublisherHandleId = screenSharePublisherHandleId,
    subscriberHandleId = subscriberHandleId
)

private fun DomainPublisherFeedResponse.toWebRtc() = PublisherFeedResponse(
    feedId = feedId,
    display = display,
    streams = streams.map { it.toWebRtc() }
)

private fun DomainPublisherFeedResponseStream.toWebRtc() = PublisherFeedResponseStream(
    type = type,
    mid = mid
)

private fun DomainSubscriberFeedResponse.toWebRtc() = SubscriberFeedResponse(
    type = type,
    mid = mid,
    feedId = feedId,
    feedMid = feedMid,
    feedDisplay = feedDisplay,
    feedDescription = feedDescription
)

private fun SubscriberFeedRequest.toDomain() = DomainSubscriberFeedRequest(
    feedId = feedId,
    mid = mid,
    crossrefid = crossrefid
)

private fun DomainTurnCredential.toWebRtc() = TurnCredential(
    username = username,
    credential = credential,
    url = url
)

private fun MediaStateMessageRequest.toDomain(): MediaStateRequest = when (this) {
    is CameraEnabledChanged -> DomainCameraEnableChanged(isCameraEnabled)
    is MicEnableChanged -> DomainMicEnableChanged(isMicEnabled)
}

private fun MediaStateResponse.toWebRtc(): MediaStateMessageResponse = when (this) {
    is DomainMediaStateChanged -> MediaStateChanged(mediaState = mediaState.toWebRtc())
    is DomainMediaStateInit -> MediaStateInit(mediaStateList = mediaStateList.map { it.toWebRtc() })
}

private fun DomainMediaState.toWebRtc(): MediaState = MediaState(
    userId = userId,
    micEnabled = micEnabled,
    cameraEnabled = cameraEnabled
)