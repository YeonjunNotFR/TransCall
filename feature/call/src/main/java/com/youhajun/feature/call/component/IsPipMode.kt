package com.youhajun.feature.call.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.util.Consumer
import com.youhajun.transcall.core.common.getActivity


@Composable
fun rememberIsInPipMode(): Boolean {
    val activity = LocalContext.current.getActivity()
    var pipMode by remember { mutableStateOf(activity?.isInPictureInPictureMode) }
    DisposableEffect(activity) {
        val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
            pipMode = info.isInPictureInPictureMode
        }
        activity?.addOnPictureInPictureModeChangedListener(
            observer
        )
        onDispose { activity?.removeOnPictureInPictureModeChangedListener(observer) }
    }
    return pipMode ?: false
}