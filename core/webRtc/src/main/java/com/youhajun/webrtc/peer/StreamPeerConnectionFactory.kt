package com.youhajun.webrtc.peer

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import com.youhajun.webrtc.model.MicChunk
import com.youhajun.webrtc.model.TurnCredential
import com.youhajun.webrtc.model.toRtpCodec
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.EglBase
import org.webrtc.HardwareVideoEncoderFactory
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.MediaStreamTrack
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpTransceiver
import org.webrtc.SimulcastVideoEncoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import org.webrtc.audio.JavaAudioDeviceModule
import javax.inject.Inject

@ServiceScoped
class StreamPeerConnectionFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eglBaseContext: EglBase.Context,
) {
    private lateinit var rtcConfig: PeerConnection.RTCConfiguration

    private val videoDecoderFactory by lazy {
        DefaultVideoDecoderFactory(eglBaseContext)
    }

    private val _micFlow = MutableSharedFlow<MicChunk>(extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val micFlow: SharedFlow<MicChunk> = _micFlow.asSharedFlow()

    private val videoEncoderFactory by lazy {
        val hardwareEncoder = HardwareVideoEncoderFactory(eglBaseContext, true, true)
        SimulcastVideoEncoderFactory(hardwareEncoder, SoftwareVideoEncoderFactory())
    }

    private val audioDeviceModule by lazy {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        JavaAudioDeviceModule.builder(context)
            .setUseHardwareAcousticEchoCanceler(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            .setUseHardwareNoiseSuppressor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            .setAudioAttributes(attrs)
            .setSamplesReadyCallback {
                if (it.data.isEmpty()) return@setSamplesReadyCallback

                val chunk = MicChunk(it.audioFormat, it.channelCount, it.sampleRate, it.data)
                _micFlow.tryEmit(chunk)
            }
            .createAudioDeviceModule().also {
                it.setMicrophoneMute(false)
                it.setSpeakerMute(false)
            }
    }

    private val factory by lazy {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions(),
        )

        PeerConnectionFactory.builder()
            .setVideoDecoderFactory(videoDecoderFactory)
            .setVideoEncoderFactory(videoEncoderFactory)
            .setAudioDeviceModule(audioDeviceModule)
            .createPeerConnectionFactory()
    }

    private val localVideoCapabilitiesCodecs by lazy {
        factory.getRtpSenderCapabilities(MediaStreamTrack.MediaType.MEDIA_TYPE_VIDEO).codecs.map {
            it.toRtpCodec()
        }
    }

    private val localAudioCapabilitiesCodecs by lazy {
        factory.getRtpSenderCapabilities(MediaStreamTrack.MediaType.MEDIA_TYPE_AUDIO).codecs.map {
            it.toRtpCodec()
        }
    }

    fun makePeerConnection(
        coroutineScope: CoroutineScope,
        type: StreamPeerType,
        mediaConstraints: MediaConstraints,
        onStreamAdded: ((MediaStream) -> Unit)? = null,
        onNegotiationNeeded: ((StreamPeerConnection, StreamPeerType) -> Unit)? = null,
        onIceCandidateRequest: ((IceCandidate?, Long) -> Unit)? = null,
        onTrack: ((RtpTransceiver?) -> Unit)? = null,
    ): StreamPeerConnection {

        val peerConnection = StreamPeerConnection(
            coroutineScope = coroutineScope,
            type = type,
            mediaConstraints = mediaConstraints,
            onStreamAdded = onStreamAdded,
            onNegotiationNeeded = onNegotiationNeeded,
            onIceCandidate = onIceCandidateRequest,
            onTrack = onTrack,
        )
        val connection = makePeerConnectionInternal(
            configuration = rtcConfig,
            observer = peerConnection,
        )
        return peerConnection.apply { initialize(connection) }
    }

    private fun makePeerConnectionInternal(
        configuration: PeerConnection.RTCConfiguration,
        observer: PeerConnection.Observer?,
    ): PeerConnection {
        return requireNotNull(
            factory.createPeerConnection(
                configuration,
                observer,
            ),
        )
    }

    fun makeVideoSource(isScreencast: Boolean): VideoSource =
        factory.createVideoSource(isScreencast)

    fun makeVideoTrack(
        source: VideoSource,
        trackId: String,
    ): VideoTrack = factory.createVideoTrack(trackId, source)

    fun makeAudioSource(constraints: MediaConstraints = MediaConstraints()): AudioSource =
        factory.createAudioSource(constraints)

    fun makeAudioTrack(
        source: AudioSource,
        trackId: String,
    ): AudioTrack = factory.createAudioTrack(trackId, source)

    fun getVideoCodec(): String? {
        return localVideoCapabilitiesCodecs.firstOrNull()?.mimeType
    }

    fun getAudioCodec(): String? {
        return localAudioCapabilitiesCodecs.firstOrNull()?.mimeType
    }

    fun initRtcConfig(turnCredential: TurnCredential) {
        val iceServers = createIceServerList(turnCredential)
        rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            iceTransportsType = PeerConnection.IceTransportsType.ALL
        }
    }

    private fun createIceServerList(turnCredential: TurnCredential) = listOf(
        PeerConnection.IceServer.builder(iceStunServerUrls).createIceServer(),
        PeerConnection.IceServer.builder(turnCredential.url).setUsername(turnCredential.username).setPassword(turnCredential.credential).createIceServer()
    )

    companion object {
        private val iceStunServerUrls = listOf(
            "stun:stun.l.google.com:3478",
        )
    }
}
