package com.youhajun.feature.call.impl.service

import android.util.Log
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.calling.payload.SignalingRequest
import com.youhajun.core.model.calling.payload.SignalingResponse
import com.youhajun.core.model.calling.payload.SttMessage
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.calling.type.MessageType
import com.youhajun.core.model.user.MyInfo
import com.youhajun.core.stt.StreamSpeechRecognizerManager
import com.youhajun.core.stt.SttResult
import com.youhajun.domain.calling.usecase.CallCloseUseCase
import com.youhajun.domain.calling.usecase.CallConnectUseCase
import com.youhajun.domain.calling.usecase.CallingSendUseCase
import com.youhajun.domain.calling.usecase.GetTurnCredentialUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.*
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
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import com.youhajun.core.model.calling.PublisherFeedResponse as DomainPublisherFeedResponse
import com.youhajun.core.model.calling.PublisherFeedResponseStream as DomainPublisherFeedResponseStream
import com.youhajun.core.model.calling.SubscriberFeedRequest as DomainSubscriberFeedRequest
import com.youhajun.core.model.calling.SubscriberFeedResponse as DomainSubscriberFeedResponse
import com.youhajun.core.model.calling.VideoRoomHandleInfo as DomainVideoRoomHandleInfo
import com.youhajun.core.model.calling.payload.CompleteIceCandidate as DomainCompleteIceCandidate
import com.youhajun.core.model.calling.payload.JoinRoomPublisher as DomainJoinRoomPublisher
import com.youhajun.core.model.calling.payload.JoinRoomSubscriber as DomainJoinRoomSubscriber
import com.youhajun.core.model.calling.payload.JoinedRoomPublisher as DomainJoinedRoomPublisher
import com.youhajun.core.model.calling.payload.OnIceCandidate as DomainOnIceCandidate
import com.youhajun.core.model.calling.payload.OnNewPublisher as DomainOnNewPublisher
import com.youhajun.core.model.calling.payload.PublisherAnswer as DomainPublisherAnswer
import com.youhajun.core.model.calling.payload.PublisherOffer as DomainPublisherOffer
import com.youhajun.core.model.calling.payload.SignalingIceCandidate as DomainIceCandidate
import com.youhajun.core.model.calling.payload.SubscriberAnswer as DomainSubscriberAnswer
import com.youhajun.core.model.calling.payload.SubscriberOffer as DomainSubscriberOffer
import com.youhajun.core.model.calling.payload.SubscriberUpdate as DomainSubscriberUpdate
import com.youhajun.core.model.calling.TurnCredential as DomainTurnCredential

@ServiceScoped
class CallServiceManager @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val webRtcManager: WebRtcSessionManager,
    private val sttManager: StreamSpeechRecognizerManager,
    private val callConnectUseCase: CallConnectUseCase,
    private val callCloseUseCase: CallCloseUseCase,
    private val callingSendUseCase: CallingSendUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getTurnCredentialUseCase: GetTurnCredentialUseCase,
) : CallServiceContract {

    interface ServiceCallback {
        fun onError(error: Throwable)
        fun onLeave()
    }

    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)
    private var serviceCallback: ServiceCallback? = null

    override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = webRtcManager.mediaUsersFlow
    private val _messageFlow = MutableSharedFlow<ServerMessage>(replay = 1, extraBufferCapacity = 50)
    override val messageFlow: SharedFlow<ServerMessage> = _messageFlow.asSharedFlow()

    internal val webRtcSignalingClient by lazy {
        object : SignalingClient {
            override suspend fun sendSignalingRequest(request: SignalingMessageRequest) {
                val msg = ClientMessage(type = MessageType.SIGNALING, payload = request.toDomain())
                callingSendUseCase(msg)
            }

            override fun observeSignalingResponse(): Flow<SignalingMessageResponse> {
                return messageFlow.mapNotNull {
                    (it.payload as? SignalingResponse)?.toWebRtc()
                }
            }
        }
    }

    private var currentUser: MyInfo? = null
    private var currentRoomId: String? = null

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
                collectSttResult(user.language)
                collectConnectRoom(roomId, user.language)
            }.onFailure { error ->
                Log.e("CallServiceManager", "startCall error", error)
                serviceCallback?.onError(error)
                dispose()
            }
        }
    }

    private fun collectSttResult(language: LanguageType) {
        scope.launch {
            sttManager.resultFlow.collect { result ->
                if (result is SttResult.Final) {
                    val payload = SttMessage(result.text, language)
                    val msg = ClientMessage(type = MessageType.TRANSLATION, payload = payload)
                    callingSendUseCase(msg)
                }
            }
        }
    }

    private fun collectConnectRoom(roomId: String, language: LanguageType) {
        scope.launch {
            callConnectUseCase(roomId).collect {
                handleServerMessage(it, language)
            }
        }
    }

    private suspend fun handleServerMessage(message: ServerMessage, language: LanguageType) {
        _messageFlow.emit(message)
        when (val payload = message.payload) {
            is ConnectedRoom -> {
                webRtcManager.start(payload.videoRoomHandleInfo.toWebRtc())
            }

            is SttStart -> {
                sttStart(language)
            }

            else -> Unit
        }
    }

    private fun sttStart(language: LanguageType) {
        val myLocale = Locale.forLanguageTag(language.code)
        sttManager.start(myLocale)
    }

    private fun dispose() {
        scope.cancel()
        webRtcManager.dispose()

        sttManager.stop()
        sttManager.destroy()
    }

    override fun leaveCall() {
        scope.launch {
            callCloseUseCase()
            serviceCallback?.onLeave()
            dispose()
        }
    }

    override fun flipCamera() = webRtcManager.flipCamera()

    override fun setCameraEnabled(enabled: Boolean) {
        webRtcManager.setCameraEnabled(enabled)
    }

    override fun setMicEnabled(enabled: Boolean) {
        webRtcManager.setMicEnabled(enabled)
        currentUser?.let { user ->
            if (enabled) sttStart(user.language) else sttManager.stop()
        }
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

private fun SignalingMessageRequest.toDomain(): SignalingRequest {
    return when (this) {
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