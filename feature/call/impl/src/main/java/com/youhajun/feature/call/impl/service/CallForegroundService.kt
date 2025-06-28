package com.youhajun.feature.call.impl.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.CallingMessage
import com.youhajun.core.model.calling.CallingMessageType
import com.youhajun.core.model.calling.MediaMessageType
import com.youhajun.core.model.calling.SignalingMessageType
import com.youhajun.core.model.calling.StageMessageType
import com.youhajun.core.model.conversation.ConversationMessageType
import com.youhajun.core.model.user.MyInfo
import com.youhajun.core.notification.call.CallNotificationHandler
import com.youhajun.core.stt.StreamSpeechRecognizerManager
import com.youhajun.core.stt.SttResult
import com.youhajun.domain.calling.usecase.CallCloseUseCase
import com.youhajun.domain.calling.usecase.CallConnectUseCase
import com.youhajun.domain.calling.usecase.CallingSendUseCase
import com.youhajun.domain.conversation.usecase.ConversationCloseUseCase
import com.youhajun.domain.conversation.usecase.ConversationConnectUseCase
import com.youhajun.domain.conversation.usecase.ConversationSendUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.SignalingMessage
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CallForegroundService : Service() {

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var webRtcFactory: WebRtcSessionManager.Factory

    @Inject
    lateinit var sttManager: StreamSpeechRecognizerManager

    @Inject
    lateinit var callConnectUseCase: CallConnectUseCase

    @Inject
    lateinit var callingSendUseCase: CallingSendUseCase

    @Inject
    lateinit var callCloseUseCase: CallCloseUseCase

    @Inject
    lateinit var conversationConnectUseCase: ConversationConnectUseCase

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

    private var currentRoomCode: String? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    private val _mediaUsersFlow = MutableStateFlow<List<CallMediaUser>>(emptyList())
    private val _messageFlow = MutableSharedFlow<CallingMessage>(replay = 1, extraBufferCapacity = 50)

    private var webRtcManager: WebRtcSessionManager? = null

    private val callServiceContract = object : CallServiceContract {
        override val mediaUsersFlow: StateFlow<List<CallMediaUser>> = _mediaUsersFlow.asStateFlow()
        override val messageFlow: SharedFlow<CallingMessage> = _messageFlow.asSharedFlow()

        override fun flipCamera() {
            webRtcManager?.flipCamera()
        }

        override fun setCameraEnabled(enabled: Boolean) {
            webRtcManager?.setCameraEnabled(enabled)
        }

        override fun setMicEnabled(enabled: Boolean) {
            webRtcManager?.setMicEnabled(enabled)
        }

        override fun setMuteEnable(enabled: Boolean) {
            webRtcManager?.setMuteEnable(enabled)
        }

        override fun setOutputEnable(
            userId: String,
            mediaContentType: MediaContentType,
            enabled: Boolean
        ) {
            webRtcManager?.setOutputEnable(userId, mediaContentType, enabled)
        }

        override fun setAudioDeviceChange(deviceType: AudioDeviceType) {
            webRtcManager?.selectAudioDevice(deviceType)
        }

        override fun callingLeft() {
            serviceScope.launch {
                callingSendUseCase(CallingMessageType.LeftRequest)
            }
        }
    }

    private val webRtcSignalingClient = object : SignalingClient {
        override fun sendOffer(offer: SignalingMessage.Offer) {
            serviceScope.launch {
                callingSendUseCase(offer.toCallingMessage())
            }
        }

        override fun sendAnswer(answer: SignalingMessage.Answer) {
            serviceScope.launch {
                callingSendUseCase(answer.toCallingMessage())
            }
        }

        override fun sendIceCandidate(candidate: SignalingMessage.IceCandidate) {
            serviceScope.launch {
                callingSendUseCase(candidate.toCallingMessage())
            }
        }

        override fun observeSignalingMsg(): Flow<SignalingMessage> {
            return callServiceContract.messageFlow.mapNotNull { it.toSignalingMessage() }
        }

        override fun observeMediaMsg(): Flow<MediaMessage> {
            return callServiceContract.messageFlow.mapNotNull { it.toMediaMessage() }
        }
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (currentRoomCode != null) return START_REDELIVER_INTENT
        val roomCode = intent?.getStringExtra(INTENT_KEY_ROOM_CODE) ?: return START_REDELIVER_INTENT
        currentRoomCode = roomCode
        val notification = createNotification(roomCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val flag = ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            startForeground(DEFAULT_SERVICE_ID, notification, flag)
        } else {
            startForeground(DEFAULT_SERVICE_ID, notification)
        }
        initCalling(roomCode)

        return START_REDELIVER_INTENT
    }

    private fun createNotification(roomCode: String): Notification {
        callNotificationHandler.registerChannel()
        return callNotificationHandler.getCallingNotification(
            openIntent = callIntentFactory.goToCallActivity(this, roomCode),
            endIntent = callIntentFactory.callService(this)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
        sttManager.stop()
        sttManager.destroy()
        webRtcManager?.dispose()
    }

    private fun initCalling(roomCode: String) = serviceScope.launch {
        getMyInfoUseCase().onSuccess { user ->
            webRtcManager = webRtcFactory.create(webRtcSignalingClient, user.userId)
            callMediaUsersCollect()
            sttResultCollect(user.language)
            callConnecting(roomCode, user)
            conversationConnecting(roomCode)
        }.onFailure {
            stopSelf()
        }
    }

    private fun callMediaUsersCollect() {
        serviceScope.launch {
            webRtcManager?.mediaUsersFlow?.collect {
                _mediaUsersFlow.value = it
            }
        }
    }

    private fun sttResultCollect(language: LanguageType) {
        serviceScope.launch {
            sttManager.resultFlow.collect { result ->
                if (result is SttResult.Final) {
                    val roomCode = currentRoomCode ?: return@collect
                    val msg = ConversationMessageType.SttMessage(roomCode, result.text, language)
                    conversationSendUseCase(msg)
                }
            }
        }
    }

    private fun conversationConnecting(roomCode: String) {
        conversationConnectUseCase(roomCode).launchIn(serviceScope)
    }

    private fun callConnecting(roomCode: String, myInfo: MyInfo) {
        serviceScope.launch {
            callConnectUseCase(roomCode).collect {
                when(val type = it.type) {
                    is StageMessageType.Signaling -> callingStart(type.isCaller, myInfo.language)
                    is CallingMessageType.LeftResponse -> {
                        if(type.userId == myInfo.userId) callClose()
                    }
                    else -> Unit
                }
                _messageFlow.emit(it)
            }
        }
    }

    private fun callingStart(isCaller: Boolean, myLanguage: LanguageType) {
        webRtcManager?.start(isCaller)
        val myLocale = Locale.forLanguageTag(myLanguage.code)
        sttManager.start(myLocale)
    }

    private fun callClose() {
        serviceScope.launch {
            webRtcManager?.dispose()
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
        const val INTENT_KEY_ROOM_CODE = "ROOM_CODE"
        private const val DEFAULT_SERVICE_ID = 1
    }
}

private fun CallingMessage.toSignalingMessage(): SignalingMessage? {
    return when (val messageType = type) {
        is SignalingMessageType.Answer -> SignalingMessage.Answer(
            messageType.sdp,
            messageType.sessionId
        )

        is SignalingMessageType.Offer -> SignalingMessage.Offer(
            messageType.sdp,
            messageType.sessionId
        )

        is SignalingMessageType.Candidate -> SignalingMessage.IceCandidate(
            sdpMid = messageType.sdpMid,
            sdpMLineIndex = messageType.sdpMLineIndex,
            candidate = messageType.candidate,
            sessionId = messageType.sessionId
        )

        else -> null
    }
}

private fun CallingMessage.toMediaMessage(): MediaMessage? {
    return when (val messageType = type) {
        is MediaMessageType.AudioStateChange -> MediaMessage.AudioStateChange(
            userId = messageType.userId,
            isMicEnabled = messageType.isMicEnabled,
            mediaContentType = MediaContentType.valueOf(messageType.mediaContentType),
            isSpeaking = messageType.isSpeaking
        )

        is MediaMessageType.VideoStateChange -> MediaMessage.VideoStateChange(
            userId = messageType.userId,
            mediaContentType = MediaContentType.valueOf(messageType.mediaContentType),
            isVideoEnabled = messageType.isVideoEnabled
        )

        else -> null
    }
}

private fun SignalingMessage.Offer.toCallingMessage() = SignalingMessageType.Offer(sdp, sessionId)

private fun SignalingMessage.Answer.toCallingMessage() = SignalingMessageType.Answer(sdp, sessionId)

private fun SignalingMessage.IceCandidate.toCallingMessage() =
    SignalingMessageType.Candidate(candidate, sdpMid, sdpMLineIndex, sessionId)