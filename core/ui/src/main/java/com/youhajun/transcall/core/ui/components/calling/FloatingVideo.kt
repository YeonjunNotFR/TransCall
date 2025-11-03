package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun FloatingVideo(
    modifier: Modifier = Modifier,
    videoTrack: VideoTrack?,
    parentBounds: IntSize,
    isFrontCamera: Boolean,
    isMicEnable: Boolean,
    cameraEnabled: Boolean,
    firstAlign: Alignment = Alignment.TopEnd,
    paddingValues: PaddingValues = PaddingValues(0.dp, 0.dp),
    rendererEvent: RendererCommon.RendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() = Unit
        override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
    },
    placeholder: @Composable () -> Unit = {}
) {
    val dragModifier = Modifier
        .dragInParentArea(firstAlign, parentBounds, paddingValues)
        .padding(paddingValues)
        .then(modifier)

    Box(modifier = dragModifier) {
        if (cameraEnabled && videoTrack != null) {
            TextureVideoRenderer(
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
                    .padding(6.dp)
                    .background(Colors.Black, CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_call_mic_off),
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun Modifier.dragInParentArea(
    firstAlign: Alignment,
    parentBounds: IntSize,
    paddingValues: PaddingValues
): Modifier {

    var dragPosition by rememberSaveable(stateSaver = OffsetSaver) {
        mutableStateOf(Offset(0f, 0f))
    }
    var dragAlignString by rememberSaveable { mutableStateOf(firstAlign.toString()) }
    val dragAlign: Alignment = remember(dragAlignString) { dragAlignString.toAlignment() }
    val offset by animateOffsetAsState(targetValue = dragPosition, label = "")
    var currentCompSize by remember { mutableStateOf(IntSize(0, 0)) }

    val startPadding = paddingValues.calculateStartPadding(LayoutDirection.Ltr).value
    val endPadding = paddingValues.calculateEndPadding(LayoutDirection.Ltr).value
    val topPadding = paddingValues.calculateTopPadding().value
    val bottomPadding = paddingValues.calculateBottomPadding().value
    val boundaryWidth = parentBounds.width - currentCompSize.width - endPadding
    val boundaryHeight = parentBounds.height - currentCompSize.height - bottomPadding

    val topStartPosition = Offset(startPadding, topPadding)
    val topEndPosition = Offset(boundaryWidth, topPadding)
    val bottomStartPosition = Offset(startPadding, boundaryHeight)
    val bottomEndPosition = Offset(boundaryWidth, boundaryHeight)

    LaunchedEffect(parentBounds, dragAlign) {
        dragPosition = calculateDragPosition(
            dragAlign,
            topStartPosition,
            topEndPosition,
            bottomStartPosition,
            bottomEndPosition
        )
    }

    return this then Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .pointerInput(parentBounds) {
            this.detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragPosition =
                        Offset(dragPosition.x + dragAmount.x, dragPosition.y + dragAmount.y)
                },
                onDragEnd = {
                    val align = calculateDragDropAlign(dragPosition, parentBounds)
                    val position = calculateDragPosition(
                        align,
                        topStartPosition,
                        topEndPosition,
                        bottomStartPosition,
                        bottomEndPosition
                    )
                    dragAlignString = align.toString()
                    dragPosition = position
                },
                onDragCancel = {
                    dragPosition = calculateDragPosition(
                        dragAlign,
                        topStartPosition,
                        topEndPosition,
                        bottomStartPosition,
                        bottomEndPosition
                    )
                }
            )
        }.onGloballyPositioned { currentCompSize = it.size }
}

private fun calculateDragPosition(
    dragAlign: Alignment,
    topStartPosition: Offset,
    topEndPosition: Offset,
    bottomStartPosition: Offset,
    bottEndPosition: Offset
): Offset {
    return when (dragAlign) {
        Alignment.TopStart -> topStartPosition
        Alignment.TopEnd -> topEndPosition
        Alignment.BottomStart -> bottomStartPosition
        Alignment.BottomEnd -> bottEndPosition
        else -> bottEndPosition
    }
}

private fun calculateDragDropAlign(dragPosition: Offset, parentBounds: IntSize): Alignment {
    val parentHalfWidth = parentBounds.width / 2
    val parentHalfHeight = parentBounds.height / 2

    val topStart = dragPosition.x < parentHalfWidth && dragPosition.y < parentHalfHeight
    val topEnd = dragPosition.x > parentHalfWidth && dragPosition.y < parentHalfHeight
    val bottomStart = dragPosition.x < parentHalfWidth && dragPosition.y > parentHalfHeight
    val bottomEnd = dragPosition.x > parentHalfWidth && dragPosition.y > parentHalfHeight

    return when {
        topStart -> Alignment.TopStart
        topEnd -> Alignment.TopEnd
        bottomStart -> Alignment.BottomStart
        bottomEnd -> Alignment.BottomEnd
        else -> Alignment.BottomEnd
    }
}

private val OffsetSaver = Saver<Offset, List<Float>>(
    save = { listOf(it.x, it.y) },
    restore = { Offset(it[0], it[1]) }
)

private fun String.toAlignment(): Alignment = when (this) {
    Alignment.TopStart.toString() -> Alignment.TopStart
    Alignment.TopEnd.toString() -> Alignment.TopEnd
    Alignment.BottomStart.toString() -> Alignment.BottomStart
    Alignment.BottomEnd.toString() -> Alignment.BottomEnd
    else -> Alignment.TopEnd
}