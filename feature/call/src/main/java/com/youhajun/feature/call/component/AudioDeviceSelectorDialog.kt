package com.youhajun.feature.call.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography
import com.youhajun.core.design.R
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.webrtc.model.stream.AudioDeviceType

@Composable
internal fun AudioDeviceSelectorDialog(
    selectedDevice: AudioDeviceType,
    availableDevices: Set<AudioDeviceType>,
    onSelect: (AudioDeviceType) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.dialog_audio_device_selector_title),
                style = Typography.titleLarge,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column {
                availableDevices.forEach { device ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                onDismiss()
                                if(device != selectedDevice) onSelect(device)
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = device.displayName(),
                            modifier = Modifier.weight(1f),
                            style = Typography.bodyMedium,
                        )
                        if (device == selectedDevice) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                tint = Colors.Black
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.common_cancel),
                style = Typography.labelLarge,
                modifier = Modifier.noRippleClickable { onDismiss() }
            )
        }
    )
}

@Composable
private fun AudioDeviceType.displayName(): String {
    val res = when (this) {
        AudioDeviceType.EARPIECE -> R.string.audio_device_earpiece
        AudioDeviceType.WIRED_HEADSET -> R.string.audio_device_headset
        AudioDeviceType.BLUETOOTH -> R.string.audio_device_bluetooth
        AudioDeviceType.SPEAKER -> R.string.audio_device_speaker
        AudioDeviceType.NONE -> R.string.audio_device_none
    }
    return stringResource(res)
}

@Preview
@Composable
private fun AudioDeviceSelectorDialogPreview() {
    AudioDeviceSelectorDialog(
        selectedDevice = AudioDeviceType.SPEAKER,
        availableDevices = setOf(
            AudioDeviceType.EARPIECE,
            AudioDeviceType.WIRED_HEADSET,
            AudioDeviceType.BLUETOOTH,
            AudioDeviceType.SPEAKER
        ),
        onSelect = {},
        onDismiss = {}
    )
}