package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun NormalVideo(
    modifier: Modifier = Modifier,
    videoTrack: VideoTrack?,
    isFrontCamera: Boolean,
    isMicEnable: Boolean,
    cameraEnabled: Boolean,
    rendererEvent: RendererCommon.RendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() = Unit
        override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
    },
    placeholder: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        if (cameraEnabled && videoTrack != null) {
            VideoRenderer(
                modifier = Modifier.fillMaxSize(),
                videoTrack = videoTrack,
                isFrontCamera = isFrontCamera,
                rendererEvents = rendererEvent,
            )
        } else {
            placeholder()
        }

        if (!isMicEnable) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(Colors.Black, CircleShape)
                    .padding(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_call_mic_off),
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}