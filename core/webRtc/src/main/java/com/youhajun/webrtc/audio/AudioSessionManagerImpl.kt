package com.youhajun.webrtc.audio

import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.Config
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.LocalAudioEvent
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.RemoteAudioStream
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.MediaConstraints
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class AudioSessionManagerImpl @Inject constructor(
    private val peerConnectionFactory: StreamPeerConnectionFactory,
    private val audioDeviceController: AudioDeviceController,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val audioStreamStore: AudioStreamStore
) : AudioSessionManager {

    private val audioConstraints: MediaConstraints by lazy(::buildAudioConstraints)

    private val audioSource by lazy {
        peerConnectionFactory.makeAudioSource(audioConstraints)
    }

    private val localAudioTrack: AudioTrack by lazy {
        val trackId = Config.getAudioTrackId(MediaContentType.DEFAULT)
        peerConnectionFactory.makeAudioTrack(source = audioSource, trackId = trackId)
    }

    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    override val audioStreamsFlow: StateFlow<List<CallAudioStream>> = audioStreamStore.audioStreamsFlow

    private val _localAudioEvent: MutableSharedFlow<LocalAudioEvent> = MutableSharedFlow(extraBufferCapacity = 1)
    override val localAudioEvent: SharedFlow<LocalAudioEvent> = _localAudioEvent.asSharedFlow()

    override fun startAudio(localUserId: String): AudioTrack {
        audioDeviceChangeCollect(localUserId)
        audioLevelCollect(localUserId)
        return localAudioTrack.also {
            val localStream = LocalAudioStream(
                userId = localUserId,
                audioTrack = localAudioTrack,
                isMicEnabled = localAudioTrack.enabled(),
                mediaContentType = MediaContentType.DEFAULT.type
            )
            audioStreamStore.upsert(localStream)
            audioDeviceController.start()
        }
    }

    override fun dispose() {
        scope.cancel()
        audioDeviceController.stop()

        runCatching { audioSource.dispose() }
        runCatching { localAudioTrack.dispose() }
    }

    override fun setMicEnabled(localUserId: String, enabled: Boolean) {
        localAudioTrack.setEnabled(enabled)
        audioStreamStore.update(localUserId, MediaContentType.DEFAULT) {
            (it as LocalAudioStream).copy(
                isMicEnabled = enabled
            )
        }
        _localAudioEvent.tryEmit(LocalAudioEvent.MicEnableChanged(enabled))
    }

    override fun selectAudioDevice(deviceType: AudioDeviceType) {
        audioDeviceController.select(deviceType)
    }

    override fun setMuteEnable(enabled: Boolean) {
        audioStreamStore.updateAll {
            it.audioTrack?.setEnabled(enabled)
            when(it) {
                is LocalAudioStream -> it.copy(
                    isMute = enabled,
                )
                is RemoteAudioStream -> it.copy(
                    isOutputEnabled = enabled,
                )
            }
        }
    }

    override fun setOutputEnable(
        userId: String,
        mediaContentType: MediaContentType,
        enabled: Boolean
    ) {
        audioStreamStore.update(userId, mediaContentType) {
            (it as RemoteAudioStream).copy(
                isOutputEnabled = enabled
            )
        }
    }

    override fun setAudioStateChange(state: MediaMessage.AudioStateChange) {
        val mediaType = MediaContentType.fromType(state.mediaContentType)
        audioStreamStore.update(state.userId, mediaType) {
            (it as RemoteAudioStream).copy(
                isMicEnabled = state.isMicEnabled,
//                isSpeaking = state.isSpeaking
            )
        }
    }

    override fun addRemoteAudioTrack(remoteAudio: RemoteAudioStream) {
        audioStreamStore.upsert(remoteAudio)
    }

    private fun buildAudioConstraints(): MediaConstraints {
        val mediaConstraints = MediaConstraints().apply {
            mandatory.addAll(
                listOf(
                    MediaConstraints.KeyValuePair("googEchoCancellation", "true"),
                    MediaConstraints.KeyValuePair("googAutoGainControl", "true"),
                    MediaConstraints.KeyValuePair("googHighpassFilter", "true"),
                    MediaConstraints.KeyValuePair("googNoiseSuppression", "true"),
                    MediaConstraints.KeyValuePair("googTypingNoiseDetection", "true")
                )
            )
        }

        return mediaConstraints.apply {
            with(optional) {
                add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
            }
        }
    }

    private fun audioDeviceChangeCollect(localUserId: String) {
        scope.launch {
            audioDeviceController.audioDeviceState.collect { state ->
                audioStreamStore.update(localUserId, MediaContentType.DEFAULT) {
                    (it as LocalAudioStream).copy(
                        selectedDevice = state.selectedDevice,
                        availableDevices = state.availableDevices
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun audioLevelCollect(localUserId: String) {
        scope.launch {
            peerConnectionFactory.micFlow.conflate().sample(300.milliseconds).collect {
                val audioLevel = if(localAudioTrack.enabled()) {
                    val rawLevel = SoundUtil.calculateAudioLevel(it)
                    SoundUtil.getSmoothedLevel(rawLevel)
                } else {
                    0.0f
                }

                audioStreamStore.update(localUserId, MediaContentType.DEFAULT) { stream ->
                    (stream as LocalAudioStream).copy(
                        audioLevel = audioLevel
                    )
                }
            }
        }
    }
}