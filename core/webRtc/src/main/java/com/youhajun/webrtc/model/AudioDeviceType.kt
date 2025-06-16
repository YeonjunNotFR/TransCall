package com.youhajun.webrtc.model

import com.twilio.audioswitch.AudioDevice

enum class AudioDeviceType {
    SPEAKER, EARPIECE, WIRED_HEADSET, BLUETOOTH, NONE;

    companion object {
        fun fromDevice(device: AudioDevice?): AudioDeviceType {
            return when (device) {
                is AudioDevice.Earpiece -> EARPIECE
                is AudioDevice.Speakerphone -> SPEAKER
                is AudioDevice.WiredHeadset -> WIRED_HEADSET
                is AudioDevice.BluetoothHeadset -> BLUETOOTH
                else -> NONE
            }
        }
    }
}