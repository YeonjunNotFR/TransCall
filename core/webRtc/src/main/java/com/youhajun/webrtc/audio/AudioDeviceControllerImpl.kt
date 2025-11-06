package com.youhajun.webrtc.audio

import android.content.Context
import com.twilio.audioswitch.AudioDevice
import com.twilio.audioswitch.AudioSwitch
import com.youhajun.webrtc.model.local.AudioDeviceState
import com.youhajun.webrtc.model.stream.AudioDeviceType
import com.youhajun.webrtc.model.stream.AudioDeviceType.BLUETOOTH
import com.youhajun.webrtc.model.stream.AudioDeviceType.EARPIECE
import com.youhajun.webrtc.model.stream.AudioDeviceType.NONE
import com.youhajun.webrtc.model.stream.AudioDeviceType.SPEAKER
import com.youhajun.webrtc.model.stream.AudioDeviceType.WIRED_HEADSET
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class AudioDeviceControllerImpl @Inject constructor(
    @ApplicationContext context: Context,
) : AudioDeviceController {
    private val audioSwitch = AudioSwitch(context)

    private val _audioDeviceState = MutableStateFlow(AudioDeviceState())
    override val audioDeviceState: StateFlow<AudioDeviceState> = _audioDeviceState.asStateFlow()

    override fun start() {
        audioSwitch.setAudioDeviceChangeListener { audioDevices, selectedAudioDevice ->
            val audioDeviceState = AudioDeviceState(
                selectedDevice = AudioDeviceType.fromDevice(selectedAudioDevice),
                availableDevices = audioDevices.map { AudioDeviceType.fromDevice(it) }.toSet()
            )
            _audioDeviceState.update { audioDeviceState }
        }
        audioSwitch.start { audioDevices, selectedAudioDevice ->
            val audioDeviceState = AudioDeviceState(
                selectedDevice = AudioDeviceType.fromDevice(selectedAudioDevice),
                availableDevices = audioDevices.map { AudioDeviceType.fromDevice(it) }.toSet()
            )
            _audioDeviceState.update { audioDeviceState }
        }
        audioSwitch.activate()
    }

    override fun stop() {
        audioSwitch.deactivate()
        audioSwitch.stop()
        audioSwitch.setAudioDeviceChangeListener(null)
    }

    override fun select(device: AudioDeviceType) {
        getDevice(device, audioSwitch.availableAudioDevices)?.let {
            audioSwitch.selectDevice(it)
        }
    }

    private fun getDevice(deviceType: AudioDeviceType, availableDevices: List<AudioDevice>): AudioDevice? {
        return when (deviceType) {
            SPEAKER -> availableDevices.find { it is AudioDevice.Speakerphone }
            EARPIECE -> availableDevices.find { it is AudioDevice.Earpiece }
            WIRED_HEADSET -> availableDevices.find { it is AudioDevice.WiredHeadset }
            BLUETOOTH -> availableDevices.find { it is AudioDevice.BluetoothHeadset }
            NONE -> null
        }
    }

}