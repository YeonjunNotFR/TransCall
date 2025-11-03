package com.youhajun.webrtc.audio

import com.youhajun.transcall.core.common.IoDispatcher
import com.youhajun.webrtc.Config
import com.youhajun.webrtc.model.stream.AudioDeviceType
import com.youhajun.webrtc.model.stream.CallAudioStream
import com.youhajun.webrtc.model.local.LocalAudioEvent
import com.youhajun.webrtc.model.stream.LocalAudioStream
import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.media.MediaState
import com.youhajun.webrtc.model.local.MicChunk
import com.youhajun.webrtc.model.stream.RemoteAudioStream
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.MediaConstraints
import javax.inject.Inject

internal class AudioSessionManagerImpl @Inject constructor(
    private val peerConnectionFactory: StreamPeerConnectionFactory,
    private val audioDeviceController: AudioDeviceController,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val audioStreamStore: AudioStreamStore
) : AudioSessionManager {

    private val smootherMap = mutableMapOf<String, SoundLevelSmoother>()
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

    private val _myAudioPcmFlow: MutableSharedFlow<ByteArray> = MutableSharedFlow(extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val myAudioPcmFlow: SharedFlow<ByteArray> = _myAudioPcmFlow.asSharedFlow()

    override fun startAudio(localUserId: String): AudioTrack {
        audioDeviceChangeCollect(localUserId)
        localMicChunkCollect(localUserId)
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
        audioStreamStore.updateDefaultLocal(localUserId) { it.copy(isMicEnabled = enabled) }
        _localAudioEvent.tryEmit(LocalAudioEvent.MicEnableChanged(enabled))
    }

    override fun selectAudioDevice(deviceType: AudioDeviceType) {
        audioDeviceController.select(deviceType)
    }

    override fun setMuteEnable(enabled: Boolean) {
        audioStreamStore.updateAll {
            it.audioTrack?.setEnabled(enabled)
            when(it) {
                is LocalAudioStream -> it.copy(isMute = enabled)
                is RemoteAudioStream -> it.copy(isOutputEnabled = enabled)
            }
        }
    }

    override fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean) {
        audioStreamStore.updateDefaultRemote(userId) {
            it.copy(isOutputEnabled = enabled)
        }
    }

    override fun addRemoteAudioTrack(remoteAudio: RemoteAudioStream) {
        if(remoteAudio.audioTrack == null) return
        audioStreamStore.upsert(remoteAudio)
        remoteAudioAddSink(remoteAudio.audioTrack, remoteAudio.userId)
    }

    override fun removeRemoteAudioTrack(userId: String, mediaContentType: MediaContentType) {
        audioStreamStore.remove(userId, mediaContentType)
    }

    override fun onMediaStateChanged(state: MediaState) {
        audioStreamStore.updateDefaultRemote(state.userId) {
            it.copy(isMicEnabled = state.micEnabled)
        }
    }

    override fun onMediaStateInit(list: List<MediaState>) {
        list.forEach { onMediaStateChanged(it) }
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
                audioStreamStore.updateDefaultLocal(localUserId) {
                    it.copy(
                        selectedDevice = state.selectedDevice,
                        availableDevices = state.availableDevices
                    )
                }
            }
        }
    }

    private fun remoteAudioAddSink(audioTrack: AudioTrack, userId: String) {
        audioTrack.addSink { audioData, bitsPerSample, sampleRate, numberOfChannels, numberOfFrames, _ ->
            val byteArray = ByteArray(audioData.remaining())
            audioData.get(byteArray)

            val chunk = MicChunk(
                audioData = byteArray,
                sampleRate = sampleRate,
                numberOfChannels = numberOfChannels,
                numberOfFrames = numberOfFrames
            )

            updateAudioLevel(chunk, userId, audioTrack.enabled())
        }
    }

    private fun localMicChunkCollect(localUserId: String) {
        scope.launch {
            peerConnectionFactory.localMicChunk.conflate().collect { chunk ->
                updateAudioLevel(chunk, localUserId, localAudioTrack.enabled())
                _myAudioPcmFlow.tryEmit(chunk.audioData)
            }
        }
    }

    private fun updateAudioLevel(chunk: MicChunk, userId: String, trackEnable: Boolean) {
        val smoother = getOrCreateSmoother(userId)
        val rawLevel = if(trackEnable) SoundUtil.calculateAudioLevel(chunk) else 0.0f
        val audioLevel = smoother.getSmoothedLevel(rawLevel)

        audioStreamStore.update(userId, MediaContentType.DEFAULT) { stream ->
            when(stream) {
                is LocalAudioStream -> stream.copy(audioLevel = audioLevel)
                is RemoteAudioStream -> stream.copy(audioLevel = audioLevel)
            }
        }
    }

    private fun getOrCreateSmoother(userId: String): SoundLevelSmoother {
        return smootherMap.getOrPut(userId) { SoundLevelSmoother() }
    }
}