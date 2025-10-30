package com.youhajun.feature.call.model;

import androidx.annotation.DrawableRes
import com.youhajun.core.design.R
import com.youhajun.webrtc.model.stream.AudioDeviceType

sealed class CallControlAction {
    data class SelectAudioDevice(
        val currentDevice: AudioDeviceType,
    ) : CallControlAction() {
        val isSpeakerEnabled: Boolean = currentDevice == AudioDeviceType.SPEAKER

        @DrawableRes val iconResId: Int = when (currentDevice) {
            AudioDeviceType.SPEAKER -> R.drawable.ic_call_speaker_on
            AudioDeviceType.EARPIECE -> R.drawable.ic_call_speaker_on
            AudioDeviceType.BLUETOOTH -> R.drawable.ic_call_speaker_on
            AudioDeviceType.WIRED_HEADSET -> R.drawable.ic_call_speaker_on
            AudioDeviceType.NONE -> R.drawable.ic_call_speaker_on
        }
    }

    data class ToggleCameraEnable(
        val isCameraEnabled: Boolean
    ) : CallControlAction()

    data class ToggleMicEnable(
        val isMicEnabled: Boolean
    ) : CallControlAction()

    data class FlipCamera(
        val isFront: Boolean
    ) : CallControlAction()

    data object LeaveCall : CallControlAction()
}
