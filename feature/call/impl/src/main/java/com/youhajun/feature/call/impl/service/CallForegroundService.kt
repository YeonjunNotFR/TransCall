package com.youhajun.feature.call.impl.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.calling.payload.SignalingRequest
import com.youhajun.core.model.calling.payload.SignalingResponse
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.calling.type.MessageType
import com.youhajun.core.model.conversation.ConversationMessageType
import com.youhajun.core.model.user.MyInfo
import com.youhajun.core.notification.call.CallNotificationHandler
import com.youhajun.core.stt.StreamSpeechRecognizerManager
import com.youhajun.core.stt.SttResult
import com.youhajun.domain.calling.usecase.CallCloseUseCase
import com.youhajun.domain.calling.usecase.CallConnectUseCase
import com.youhajun.domain.calling.usecase.CallingSendUseCase
import com.youhajun.domain.conversation.usecase.ConversationCloseUseCase
import com.youhajun.domain.conversation.usecase.ConversationSendUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.CompleteIceCandidate
import com.youhajun.webrtc.model.JoinRoomPublisher
import com.youhajun.webrtc.model.JoinRoomSubscriber
import com.youhajun.webrtc.model.JoinedRoomPublisher
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
import com.youhajun.webrtc.model.VideoRoomHandleInfo
import com.youhajun.webrtc.session.WebRtcSessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

@AndroidEntryPoint
class CallForegroundService : Service() {

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var sttManager: StreamSpeechRecognizerManager

    @Inject
    lateinit var callConnectUseCase: CallConnectUseCase

    @Inject
    lateinit var callingSendUseCase: CallingSendUseCase

    @Inject
    lateinit var callCloseUseCase: CallCloseUseCase

    @Inject
    lateinit var conversationCloseUseCase: ConversationCloseUseCase

    @Inject
    lateinit var conversationSendUseCase: ConversationSendUseCase

    @Inject
    lateinit var getMyInfoUseCase: GetMyInfoUseCase

    @Inject
    lateinit var callNotificationHandler: CallNotificationHandler

    @Inject
    lateinit var callIntentFactory: CallIntentFactory

    @Inject
    lateinit var webRtcManager: WebRtcSessionManager

    private var currentRoomId: String? = null
    private var serviceScope: CoroutineScope? = null

    private val _mediaUsersFlow = MutableStateFlow<List<CallMediaUser>>(emptyList())
    private val _messageFlow = MutableSharedFlow<ServerMessage>(replay = 1, extraBufferCapacity = 50)

    private var myLanguageType: LanguageType = LanguageType.ENGLISH

    override fun onCreate() {
        super.onCreate()

        serviceScope = CoroutineScope(SupervisorJob() + ioDispatcher)
    }

    private val callServiceContract = object : CallServiceContract {
        override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = _mediaUsersFlow.asStateFlow()
        override val messageFlow: SharedFlow<ServerMessage> = _messageFlow.asSharedFlow()

        override fun flipCamera() {
            webRtcManager.flipCamera()
        }

        override fun setCameraEnabled(enabled: Boolean) {
            webRtcManager.setCameraEnabled(enabled)
        }

        override fun setMicEnabled(enabled: Boolean) {
            webRtcManager.setMicEnabled(enabled)
            if(enabled) sttStart(myLanguageType) else sttManager.stop()
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

        override fun callingLeft() {
            serviceScope?.launch {
//                callingSendUseCase(CallingMessageType.LeftRequest)
            }
        }
    }

    internal val webRtcSignalingClient by lazy {
        object : SignalingClient {
            override suspend fun sendSignalingRequest(request: SignalingMessageRequest) {
                serviceScope?.launch {
                    val msg = ClientMessage(type = MessageType.SIGNALING, payload = request.toDomain())
                    callingSendUseCase(msg)
                }
            }

            override fun observeSignalingResponse(): Flow<SignalingMessageResponse> {
                return callServiceContract.messageFlow.mapNotNull {
                    (it.payload as? SignalingResponse)?.toWebRtc()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleAction(intent)
        if (currentRoomId != null) return START_REDELIVER_INTENT
        val roomId = intent?.getStringExtra(INTENT_KEY_ROOM_ID) ?: return START_REDELIVER_INTENT
        currentRoomId = roomId
        val notification = createNotification(roomId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val flag = ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            startForeground(DEFAULT_SERVICE_ID, notification, flag)
        } else {
            startForeground(DEFAULT_SERVICE_ID, notification)
        }
        initCalling(roomId)

        return START_REDELIVER_INTENT
    }

    private fun handleAction(intent: Intent?) {
        when (intent?.action) {
            INTENT_ACTION_CALL_LEFT -> {
                serviceScope?.launch {
//                    callingSendUseCase(CallingMessageType.LeftRequest)
                }
            }
            else -> Unit
        }
    }

    private fun createNotification(roomId: String): Notification {
        callNotificationHandler.registerChannel()
        return callNotificationHandler.getCallingNotification(
            openIntent = callIntentFactory.getCallActivityIntent(this, roomId).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            endIntent = callIntentFactory.getCallServiceIntent(this, roomId)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope?.cancel()
        sttManager.stop()
        sttManager.destroy()
        webRtcManager.dispose()
    }

    private fun initCalling(roomId: String) = serviceScope?.launch {
        getMyInfoUseCase().onSuccess { user ->
            callMediaUsersCollect()
            sttResultCollect(user.language)
            callConnecting(roomId, user)
        }.onFailure {
            stopSelf()
        }
    }

    private fun callMediaUsersCollect() {
        serviceScope?.launch {
            webRtcManager.mediaUsersFlow.collect {
                _mediaUsersFlow.value = it
            }
        }
    }

    private fun sttResultCollect(language: LanguageType) {
        serviceScope?.launch {
            sttManager.resultFlow.collect { result ->
                if (result is SttResult.Final) {
                    val roomId = currentRoomId ?: return@collect
                    val msg = ConversationMessageType.SttMessage(roomId, result.text, language)
                    conversationSendUseCase(msg)
                }
            }
        }
    }

    private fun callConnecting(roomId: String, myInfo: MyInfo) {
        serviceScope?.launch {
            callConnectUseCase(roomId).collect {
                when(val type = it.payload) {
                    is ConnectedRoom -> {
                        webRtcManager.start(myInfo.userId, type.videoRoomHandleInfo.toWebRtc())
                    }

                    is SttStart -> {
                        sttStart(myInfo.language)
                    }

//                    is CallingMessageType.LeftResponse -> {
//                        if(type.userId == myInfo.userId) callClose()
//                    }
                    else -> Unit
                }
                _messageFlow.emit(it)
            }
        }
    }

    private fun sttStart(language: LanguageType) {
        myLanguageType = language
        val myLocale = Locale.forLanguageTag(language.code)
        sttManager.start(myLocale)
    }

    private fun callClose() {
        serviceScope?.launch {
            webRtcManager.dispose()
            callCloseUseCase()
            conversationCloseUseCase()
            sttManager.stop()
            stopSelf()
        }
    }

    inner class LocalBinder : Binder() {
        fun getContract(): CallServiceContract = callServiceContract
    }

    companion object {
        const val INTENT_KEY_ROOM_ID = "ROOM_ID"
        const val INTENT_ACTION_CALL_LEFT = "ACTION_CALL_LEFT"
        private const val DEFAULT_SERVICE_ID = 1
    }
}

//private fun CallingMessage.toMediaMessage(): MediaMessage? {
//    return when (val messageType = type) {
//        is MediaMessageType.AudioStateChange -> MediaMessage.AudioStateChange(
//            userId = messageType.userId,
//            isMicEnabled = messageType.isMicEnabled,
//            mediaContentType = MediaContentType.valueOf(messageType.mediaContentType),
//            isSpeaking = messageType.isSpeaking
//        )
//
//        is MediaMessageType.VideoStateChange -> MediaMessage.VideoStateChange(
//            userId = messageType.userId,
//            mediaContentType = MediaContentType.valueOf(messageType.mediaContentType),
//            isVideoEnabled = messageType.isVideoEnabled
//        )
//
//        else -> null
//    }
//}

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