package com.youhajun.feature.call.impl.calling

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.impl.component.AudioDeviceSelectorDialog
import com.youhajun.feature.call.impl.component.BottomCallController
import com.youhajun.feature.call.impl.component.RoomCodeComp
import com.youhajun.feature.call.impl.model.CallControlAction
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.feature.call.impl.service.LocalCallServiceContract
import com.youhajun.hyanghae.graphics.modifier.conditional
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.calling.DefaultVideoPlaceHolder
import com.youhajun.transcall.core.ui.components.calling.FloatingVideo
import com.youhajun.transcall.core.ui.components.calling.NormalVideo
import com.youhajun.transcall.core.ui.components.calling.SubtitleStack
import com.youhajun.transcall.core.ui.components.modifier.speakingGlow
import com.youhajun.webrtc.model.LocalVideoStream
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun CallingRoute(
    viewModel: CallingViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val callServiceContract = LocalCallServiceContract.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(callServiceContract) {
        viewModel.setCallServiceContract(callServiceContract)
    }

    viewModel.collectSideEffect {
        when (it) {
            is CallingSideEffect.Navigation -> onNavigate(it.navigationEvent)
        }
    }

    val myAudioStream = state.myAudioStream
    if(myAudioStream != null && state.isShowAudioDeviceChangeDialog) {
        AudioDeviceSelectorDialog(
            selectedDevice = myAudioStream.selectedDevice,
            availableDevices = myAudioStream.availableDevices,
            onSelect = viewModel::onSelectAudioDevice,
            onDismiss = viewModel::onDismissAudioDeviceSelector
        )
    }

    CallingScreen(
        state = state,
        onClickCallAction = viewModel::onClickCallAction,
        onClickRoomCodeCopy = viewModel::onClickRoomCodeCopy,
        onClickRoomCodeShare = viewModel::onClickRoomCodeShare,
        onTabCallingScreen = viewModel::onTabCallingScreen,
        onDoubleTapGrid = viewModel::onDoubleTapGrid,
        onDoubleTapFloating = viewModel::onDoubleTapFloating,
        onDoubleTapFull = viewModel::onDoubleTapFull
    )
}

@Composable
internal fun CallingScreen(
    state: CallingState,
    onClickCallAction: (CallControlAction) -> Unit,
    onClickRoomCodeCopy: () -> Unit,
    onClickRoomCodeShare: () -> Unit,
    onTabCallingScreen: () -> Unit,
    onDoubleTapGrid: (CallUserUiModel) -> Unit,
    onDoubleTapFloating: () -> Unit,
    onDoubleTapFull: () -> Unit
) {
    AnimatedContent(
        modifier = Modifier.fillMaxSize(),
        targetState = state.callingScreenType,
    ) {
        when (it) {
            is CallingScreenType.Waiting -> WaitingScreenType(
                roomCode = state.roomInfo.roomCode,
                myDefaultCallUser = state.myDefaultCallUser,
                roomJoinType = state.roomInfo.joinType,
                callControlActionList = state.callControlActionList,
                onClickCallAction = onClickCallAction,
                onClickRoomCodeCopy = onClickRoomCodeCopy,
                onClickRoomCodeShare = onClickRoomCodeShare
            )
            is CallingScreenType.FloatingAndFull -> CallingScreenContainer(
                recentConversation = state.recentConversation,
                isShowBottomCallController = state.isShowBottomCallController,
                callControlActionList = state.callControlActionList,
                onClickCallAction = onClickCallAction,
                onTabCallingScreen = onTabCallingScreen,
            ) {
                CallingFloatingScreenType(
                    floatingScreenType = it,
                    onDoubleTapFloating = onDoubleTapFloating,
                    onDoubleTapFull = onDoubleTapFull
                )
            }

            is CallingScreenType.Grid -> CallingScreenContainer(
                recentConversation = state.recentConversation,
                isShowBottomCallController = state.isShowBottomCallController,
                callControlActionList = state.callControlActionList,
                onClickCallAction = onClickCallAction,
                onTabCallingScreen = onTabCallingScreen,
            ) {
                CallingGridScreenType(
                    callUserUiList = state.callUserUiModelList,
                    onDoubleTapGrid = onDoubleTapGrid
                )
            }

            CallingScreenType.Summary -> SummaryScreenType()
        }
    }
}

@Composable
private fun WaitingScreenType(
    roomCode: String,
    myDefaultCallUser: CallUserUiModel?,
    roomJoinType: RoomJoinType,
    callControlActionList: ImmutableList<CallControlAction>,
    onClickCallAction: (CallControlAction) -> Unit,
    onClickRoomCodeCopy: () -> Unit,
    onClickRoomCodeShare: () -> Unit,
) {
    val audioLevel = myDefaultCallUser?.mediaUser?.audioStream?.audioLevel ?: 0f
    val myCameraVideo = myDefaultCallUser?.mediaUser?.videoStream
    val isFrontCamera = myCameraVideo is LocalVideoStream && myCameraVideo.isFrontCamera

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Colors.FF0A0A1A, Colors.FF15152B, Colors.FF1F1B2C))
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NormalVideo(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .heightIn(max = 400.dp)
                    .conditional(myCameraVideo != null && myCameraVideo.isVideoEnable) {
                        Modifier.speakingGlow(
                            audioLevel = audioLevel,
                            shape = RoundedCornerShape(16.dp),
                            outlineWidth = 1.dp,
                            outlineAlpha = 0.5f,
                        )
                    }
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                videoTrack = myCameraVideo?.videoTrack,
                isFrontCamera = isFrontCamera,
                enabled = myCameraVideo?.isVideoEnable ?: true,
                placeholder = {
                    DefaultVideoPlaceHolder(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Colors.SurfaceDark)
                            .padding(24.dp),
                        audioLevel = myDefaultCallUser?.mediaUser?.audioStream?.audioLevel ?: 0f,
                        currentParticipant = myDefaultCallUser?.currentParticipant,
                        displayNameTextStyle = Typography.displayMedium.copy(
                            fontSize = 40.sp,
                        ),
                        languageTextStyle = Typography.bodyLarge.copy(
                            fontSize = 24.sp
                        ),
                        languageIconSize = 32.dp,
                    )
                }
            )

            if (roomJoinType == RoomJoinType.CODE_JOIN) {
                VerticalSpacer(28.dp)

                RoomCodeComp(
                    roomCode = roomCode,
                    onClickCopy = onClickRoomCodeCopy,
                    onClickShare = onClickRoomCodeShare
                )

                VerticalSpacer(28.dp)
            } else {
                VerticalSpacer(28.dp)
            }

            CircularProgressIndicator(
                modifier = Modifier.size(42.dp),
                strokeWidth = 3.dp,
                color = Colors.Gray300
            )

            VerticalSpacer(28.dp)

            Text(
                text = stringResource(R.string.waiting_other_user),
                color = Colors.White,
                style = Typography.displaySmall
            )
        }

        BottomCallController(
            callControlActionList = callControlActionList,
            onClickCallAction = onClickCallAction
        )
    }
}

@Composable
private fun CallingScreenContainer(
    recentConversation: Conversation?,
    isShowBottomCallController: Boolean,
    callControlActionList: ImmutableList<CallControlAction>,
    onClickCallAction: (CallControlAction) -> Unit,
    onTabCallingScreen: () -> Unit,
    container: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onTabCallingScreen() })
            }
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Colors.FF0A0A1A, Colors.FF15152B, Colors.FF1F1B2C))
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            container()

            SubtitleStack(
                conversation = recentConversation,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

        AnimatedVisibility(
            visible = isShowBottomCallController,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            BottomCallController(
                callControlActionList = callControlActionList,
                onClickCallAction = onClickCallAction
            )
        }
    }
}

private const val MAX_LAZY_GRID_SIZE = 4
private const val GRID_COLUMN_COUNT = 2

@Composable
private fun CallingGridScreenType(
    callUserUiList: ImmutableList<CallUserUiModel>,
    onDoubleTapGrid: (CallUserUiModel) -> Unit
) {
    if (callUserUiList.size <= MAX_LAZY_GRID_SIZE) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val columns = if (callUserUiList.size <= 2) 1 else GRID_COLUMN_COUNT
            callUserUiList.chunked(columns).forEachIndexed { idx, rowUsers ->
                key("row_$idx") {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        rowUsers.forEach { user ->
                            key(user.mediaKey) {
                                Box(modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()) {
                                    CallingGridTypeTile(
                                        callUserUiModel = user,
                                        onDoubleTapGrid = onDoubleTapGrid
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_COUNT),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = callUserUiList,
                key = { it.mediaKey },
            ) { callMediaUser ->
                CallingGridTypeTile(
                    callUserUiModel = callMediaUser,
                    onDoubleTapGrid = onDoubleTapGrid
                )
            }
        }
    }
}

@Composable
private fun CallingGridTypeTile(
    callUserUiModel: CallUserUiModel,
    onDoubleTapGrid: (CallUserUiModel) -> Unit
) {
    val audioLevel = callUserUiModel.mediaUser.audioStream.audioLevel
    val videoStream = callUserUiModel.mediaUser.videoStream
    val isFrontCamera = videoStream is LocalVideoStream && videoStream.isFrontCamera

    NormalVideo(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { onDoubleTapGrid(callUserUiModel) })
            }
            .fillMaxSize()
            .conditional(videoStream.videoTrack != null && videoStream.isVideoEnable) {
                Modifier.speakingGlow(
                    audioLevel = audioLevel,
                    shape = RoundedCornerShape(16.dp),
                    minBlurDp = 1.dp,
                    maxBlurDp = 30.dp,
                    outlineWidth = 1.dp,
                    outlineAlpha = 0.7f,
                )
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Colors.Gray300),
        videoTrack = videoStream.videoTrack,
        isFrontCamera = isFrontCamera,
        enabled = videoStream.isVideoEnable,
        placeholder = {
            DefaultVideoPlaceHolder(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.SurfaceDark)
                    .padding(24.dp),
                audioLevel = callUserUiModel.mediaUser.audioStream.audioLevel,
                currentParticipant = callUserUiModel.currentParticipant,
                displayNameTextStyle = Typography.displayMedium.copy(
                    fontSize = 20.sp,
                ),
                languageTextStyle = Typography.bodyLarge.copy(
                    fontSize = 14.sp
                ),
                languageIconSize = 24.dp,
            )
        }
    )
}

@Composable
private fun CallingFloatingScreenType(
    floatingScreenType: CallingScreenType.FloatingAndFull,
    onDoubleTapFloating: () -> Unit,
    onDoubleTapFull: () -> Unit
) {
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val floatingVideo = floatingScreenType.floatingCallUser.mediaUser.videoStream
    val fullVideo = floatingScreenType.fullCallUser.mediaUser.videoStream
    val floatingAudioLevel = floatingScreenType.floatingCallUser.mediaUser.audioStream.audioLevel
    val fullAudioLevel = floatingScreenType.fullCallUser.mediaUser.audioStream.audioLevel
    val isFloatingFrontCamera = floatingVideo is LocalVideoStream && floatingVideo.isFrontCamera
    val isFullFrontCamera = fullVideo is LocalVideoStream && fullVideo.isFrontCamera

    Box(
        modifier = Modifier
            .onSizeChanged { parentSize = it }
            .fillMaxSize()
    ) {
        NormalVideo(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { onDoubleTapFull() })
                }
                .fillMaxSize()
                .conditional(fullVideo.videoTrack != null && fullVideo.isVideoEnable) {
                    Modifier.speakingGlow(
                        audioLevel = fullAudioLevel,
                        shape = RoundedCornerShape(16.dp),
                        minBlurDp = 1.dp,
                        maxBlurDp = 30.dp,
                        outlineWidth = 1.dp,
                        outlineAlpha = 0.7f,
                    )
                }
                .clip(RoundedCornerShape(16.dp)),
            videoTrack = fullVideo.videoTrack,
            isFrontCamera = isFullFrontCamera,
            placeholder = {
                DefaultVideoPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.SurfaceDark)
                        .padding(32.dp),
                    audioLevel = floatingScreenType.fullCallUser.mediaUser.audioStream.audioLevel,
                    currentParticipant = floatingScreenType.fullCallUser.currentParticipant,
                    displayNameTextStyle = Typography.displayMedium.copy(
                        fontSize = 40.sp,
                    ),
                    languageTextStyle = Typography.bodyLarge.copy(
                        fontSize = 24.sp
                    ),
                    languageIconSize = 32.dp,
                )
            }
        )

        FloatingVideo(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { onDoubleTapFloating() })
                }
                .size(width = 100.dp, height = 150.dp)
                .speakingGlow(
                    audioLevel = floatingAudioLevel,
                    shape = RoundedCornerShape(16.dp),
                    minBlurDp = 1.dp,
                    maxBlurDp = 30.dp,
                    outlineWidth = 1.dp,
                    outlineAlpha = 0.7f,
                )
                .clip(RoundedCornerShape(16.dp)),
            videoTrack = floatingVideo.videoTrack,
            parentBounds = parentSize,
            paddingValues = PaddingValues(10.dp),
            isFrontCamera = isFloatingFrontCamera,
            placeholder = {
                DefaultVideoPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.SurfaceDark)
                        .padding(8.dp),
                    audioLevel = floatingScreenType.floatingCallUser.mediaUser.audioStream.audioLevel,
                    currentParticipant = floatingScreenType.floatingCallUser.currentParticipant,
                    displayNameTextStyle = Typography.displaySmall.copy(
                        fontSize = 20.sp
                    ),
                    languageTextStyle = Typography.bodyLarge.copy(
                        fontSize = 12.sp
                    ),
                    languageIconSize = 18.dp,
                    maxBlurValue = 24.dp,
                )
            }
        )
    }
}

@Composable
private fun SummaryScreenType(

) {

}

@Composable
@Preview
private fun WaitingScreenPreviewMirror() {
    WaitingScreenPreview()
}

@Composable
@Preview
private fun CallingScreenFloatingTypePreviewMirror() {
    CallingScreenFloatingType()
}

@Composable
@Preview
private fun CallingScreenGridTypePreviewMirror() {
    CallingScreenGridType()
}