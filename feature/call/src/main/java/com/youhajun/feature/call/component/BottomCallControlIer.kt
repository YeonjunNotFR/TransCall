package com.youhajun.feature.call.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.feature.call.model.CallControlAction
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.webrtc.model.stream.AudioDeviceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun BottomCallController(
    callControlActionList: ImmutableList<CallControlAction>,
    onClickCallAction: (CallControlAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .background(Colors.Black, shape = RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        callControlActionList.forEach {
            CallControlItem(it.toCallControlActionHolder(), onClickCallAction)
        }
    }
}

@Composable
private fun CallControlItem(
    holder: CallControlActionHolder,
    onClick: (CallControlAction) -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(holder.backgroundColor, CircleShape)
            .noRippleClickable {
                onClick(holder.callAction)
            }
    ) {
        Icon(
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.Center),
            tint = holder.iconTint,
            painter = painterResource(id = holder.icon),
            contentDescription = null
        )
    }
}

private fun CallControlAction.toCallControlActionHolder(): CallControlActionHolder = when (this) {
    is CallControlAction.ToggleMicEnable -> CallControlActionHolder(
        backgroundColor = if (isMicEnabled) Colors.FF292929 else Colors.White,
        iconTint = if (isMicEnabled) Colors.LightGray else Colors.Black,
        icon = if (isMicEnabled) R.drawable.ic_call_mic_on else R.drawable.ic_call_mic_off,
        callAction = this,
    )

    is CallControlAction.SelectAudioDevice -> CallControlActionHolder(
        backgroundColor = if (isSpeakerEnabled) Colors.White else Colors.FF292929,
        iconTint = if (isSpeakerEnabled) Colors.Black else Colors.LightGray,
        icon = iconResId,
        callAction = this,
    )

    is CallControlAction.ToggleCameraEnable -> CallControlActionHolder(
        backgroundColor = if (isCameraEnabled) Colors.FF292929 else Colors.White,
        iconTint = if (isCameraEnabled) Colors.LightGray else Colors.Black,
        icon = if (isCameraEnabled) R.drawable.ic_call_camera_on else R.drawable.ic_call_camera_off,
        callAction = this
    )

    is CallControlAction.FlipCamera -> CallControlActionHolder(
        backgroundColor = if (isFront) Colors.FF292929 else Colors.White,
        iconTint = if (isFront) Colors.LightGray else Colors.Black,
        icon = R.drawable.ic_call_camera_flip,
        callAction = this
    )

    CallControlAction.LeaveCall -> CallControlActionHolder(
        backgroundColor = Colors.FFFF0000,
        iconTint = Colors.White,
        icon = R.drawable.ic_leave_call,
        callAction = this
    )
}

private data class CallControlActionHolder(
    val backgroundColor: Color,
    val iconTint: Color,
    @param:DrawableRes val icon: Int,
    val callAction: CallControlAction,
)

@Preview
@Composable
private fun BottomCallControllerPreview() {
    BottomCallController(
        callControlActionList = persistentListOf(
            CallControlAction.ToggleMicEnable(isMicEnabled = false),
            CallControlAction.SelectAudioDevice(currentDevice = AudioDeviceType.SPEAKER),
            CallControlAction.LeaveCall,
            CallControlAction.FlipCamera(isFront = true),
            CallControlAction.ToggleCameraEnable(isCameraEnabled = true),
        ),
        onClickCallAction = {}
    )
}