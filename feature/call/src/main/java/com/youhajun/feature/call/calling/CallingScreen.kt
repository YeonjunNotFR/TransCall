package com.youhajun.feature.call.calling

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.component.AudioDeviceSelectorDialog
import com.youhajun.feature.call.component.AutoEnterPipPreS
import com.youhajun.feature.call.component.BottomCallController
import com.youhajun.feature.call.component.PipBroadcastReceiver
import com.youhajun.feature.call.component.RoomCodeComp
import com.youhajun.feature.call.component.autoEnterPipModifier
import com.youhajun.feature.call.component.rememberIsInPipMode
import com.youhajun.feature.call.model.CallControlAction
import com.youhajun.feature.call.model.CallUserUiModel
import com.youhajun.feature.call.model.CallingScreenType
import com.youhajun.feature.call.api.service.LocalCallServiceContract
import com.youhajun.hyanghae.graphics.modifier.conditional
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.calling.DefaultVideoPlaceHolder
import com.youhajun.transcall.core.ui.components.calling.FloatingVideo
import com.youhajun.transcall.core.ui.components.calling.NormalVideo
import com.youhajun.transcall.core.ui.components.calling.SubtitleStack
import com.youhajun.transcall.core.ui.components.modifier.speakingGlow
import com.youhajun.webrtc.model.stream.LocalVideoStream
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun CallingRoute(
    viewModel: CallingViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val callServiceContract = LocalCallServiceContract.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val isInPip = rememberIsInPipMode()

    viewModel.collectSideEffect {
        when (it) {
            is CallingSideEffect.Navigation -> onNavigate(it.navigationEvent)
        }
    }

    LaunchedEffect(isInPip) {
        if(isInPip) viewModel.onEnterPipMode()
        else viewModel.onExitPipMode()
    }

    LaunchedEffect(callServiceContract) {
        viewModel.setCallServiceContract(callServiceContract)
    }

    AutoEnterPipPreS(shouldAutoEnter = state.shouldAutoEnterPip)

    PipBroadcastReceiver(isInPip, viewModel::onClickCallAction)

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
        onTapCallingScreen = viewModel::onTapCallingScreen,
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
    onTapCallingScreen: () -> Unit,
    onDoubleTapGrid: (CallUserUiModel) -> Unit,
    onDoubleTapFloating: () -> Unit,
    onDoubleTapFull: () -> Unit
) {

    val holder = rememberSaveableStateHolder()
    val myCameraVideo = state.myDefaultCallUser?.mediaUser?.videoStream
    val myCameraAudio = state.myDefaultCallUser?.mediaUser?.audioStream
    val isMicEnable = myCameraAudio?.isMicEnabled ?: false
    val isCameraEnable = myCameraVideo?.isVideoEnable ?: false

    Crossfade(
        modifier = Modifier.fillMaxSize().autoEnterPipModifier(true, isCameraEnable, isMicEnable),
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

            is CallingScreenType.FloatingAndFull,
            is CallingScreenType.Grid -> CallingScreenContainer(
                recentParticipant = state.recentParticipant,
                recentConversation = state.recentConversation,
                isShowBottomCallController = state.isShowBottomCallController,
                callControlActionList = state.callControlActionList,
                onClickCallAction = onClickCallAction,
                content = {
                    if (it is CallingScreenType.Grid) {
                        holder.SaveableStateProvider("grid") {
                            CallingGridScreenType(
                                callUserUiList = state.callUserUiModelList,
                                onTapGrid = onTapCallingScreen,
                                onDoubleTapGrid = onDoubleTapGrid
                            )
                        }
                    }
                    if (it is CallingScreenType.FloatingAndFull) {
                        holder.SaveableStateProvider("floating") {
                            CallingFloatingScreenType(
                                callUserUiModelList = state.callUserUiModelList,
                                floatingScreenType = it,
                                onTapScreen = onTapCallingScreen,
                                onDoubleTapFloating = onDoubleTapFloating,
                                onDoubleTapFull = onDoubleTapFull
                            )
                        }
                    }
                }
            )

            is CallingScreenType.PipMode -> PipModeScreenType(
                callUserUiModelList = state.callUserUiModelList,
                pipModeScreenType = it
            )

            CallingScreenType.ENDED -> Unit
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
    val myCameraVideo = myDefaultCallUser?.mediaUser?.videoStream
    val myCameraAudio = myDefaultCallUser?.mediaUser?.audioStream
    val isMicEnable = myCameraAudio?.isMicEnabled ?: false
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
                        speakingGlow(
                            audioLevel = myCameraAudio?.audioLevel ?: 0f,
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
                isMicEnable = isMicEnable,
                cameraEnabled = myCameraVideo?.isVideoEnable ?: true,
                placeholder = {
                    DefaultVideoPlaceHolder(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Colors.SurfaceDark)
                            .padding(24.dp),
                        audioLevel = myDefaultCallUser?.mediaUser?.audioStream?.audioLevel ?: 0f,
                        participant = myDefaultCallUser?.participant,
                        displayNameTextStyle = Typography.displayMedium.copy(fontSize = 40.sp,),
                        languageTextStyle = Typography.bodyLarge.copy(fontSize = 24.sp),
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
    recentParticipant: Participant?,
    recentConversation: TranslationMessage?,
    isShowBottomCallController: Boolean,
    callControlActionList: ImmutableList<CallControlAction>,
    onClickCallAction: (CallControlAction) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
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
            content()

            if(recentParticipant != null && recentConversation != null) {
                SubtitleStack(
                    participant = recentParticipant,
                    conversation = recentConversation,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }

        if(isShowBottomCallController) {
            VerticalSpacer(12.dp)

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
    onTapGrid: () -> Unit,
    onDoubleTapGrid: (CallUserUiModel) -> Unit
) {
    val isLazyGrid = callUserUiList.size > MAX_LAZY_GRID_SIZE

    Crossfade(
        targetState = isLazyGrid,
        label = "CallingGridCrossfade"
    ) {
        if (it) {
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
                        onTapGrid = onTapGrid,
                        onDoubleTapGrid = onDoubleTapGrid
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val columns = if (callUserUiList.size <= 2) 1 else GRID_COLUMN_COUNT
                callUserUiList.chunked(columns).forEach { rowUsers ->
                    key(rowUsers.joinToString("-") { it.mediaKey }) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            rowUsers.forEach { user ->
                                key(user.mediaKey) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        CallingGridTypeTile(
                                            callUserUiModel = user,
                                            onTapGrid = onTapGrid,
                                            onDoubleTapGrid = onDoubleTapGrid
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallingGridTypeTile(
    callUserUiModel: CallUserUiModel,
    onTapGrid: () -> Unit,
    onDoubleTapGrid: (CallUserUiModel) -> Unit
) {
    val videoStream = callUserUiModel.mediaUser.videoStream
    val audioStream = callUserUiModel.mediaUser.audioStream
    val isFrontCamera = videoStream is LocalVideoStream && videoStream.isFrontCamera

    NormalVideo(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTapGrid() },
                    onDoubleTap = { onDoubleTapGrid(callUserUiModel) }
                )
            }
            .fillMaxSize()
            .conditional(videoStream.videoTrack != null && videoStream.isVideoEnable) {
                speakingGlow(
                    audioLevel = audioStream.audioLevel,
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
        cameraEnabled = videoStream.isVideoEnable,
        isMicEnable = audioStream.isMicEnabled,
        placeholder = {
            DefaultVideoPlaceHolder(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.SurfaceDark)
                    .padding(24.dp),
                audioLevel = callUserUiModel.mediaUser.audioStream.audioLevel,
                participant = callUserUiModel.participant,
                displayNameTextStyle = Typography.displayMedium.copy(fontSize = 20.sp),
                languageTextStyle = Typography.bodyLarge.copy(fontSize = 14.sp),
                languageIconSize = 24.dp,
            )
        }
    )
}

@Composable
private fun CallingFloatingScreenType(
    callUserUiModelList: ImmutableList<CallUserUiModel>,
    floatingScreenType: CallingScreenType.FloatingAndFull,
    onTapScreen: () -> Unit,
    onDoubleTapFloating: () -> Unit,
    onDoubleTapFull: () -> Unit
) {
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val floatingMedia = callUserUiModelList.first { it.mediaKey == floatingScreenType.floatingMediaKey }
    val fullMedia = callUserUiModelList.first { it.mediaKey == floatingScreenType.fullMediaKey }

    val floatingVideo = floatingMedia.mediaUser.videoStream
    val floatingAudio = floatingMedia.mediaUser.audioStream
    val fullVideo = fullMedia.mediaUser.videoStream
    val fullAudio = fullMedia.mediaUser.audioStream
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
                    detectTapGestures(
                        onTap = { onTapScreen() },
                        onDoubleTap = { onDoubleTapFull() }
                    )
                }
                .fillMaxSize()
                .conditional(fullVideo.videoTrack != null && fullVideo.isVideoEnable) {
                    speakingGlow(
                        audioLevel = fullAudio.audioLevel,
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
            isMicEnable = fullAudio.isMicEnabled,
            cameraEnabled = fullVideo.isVideoEnable,
            placeholder = {
                DefaultVideoPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.SurfaceDark)
                        .padding(32.dp),
                    audioLevel = fullMedia.mediaUser.audioStream.audioLevel,
                    participant = fullMedia.participant,
                    displayNameTextStyle = Typography.displayMedium.copy(fontSize = 40.sp),
                    languageTextStyle = Typography.bodyLarge.copy(fontSize = 24.sp),
                    languageIconSize = 32.dp,
                )
            }
        )

        FloatingVideo(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onTapScreen() },
                        onDoubleTap = { onDoubleTapFloating() }
                    )
                }
                .size(width = 100.dp, height = 150.dp)
                .speakingGlow(
                    audioLevel = floatingAudio.audioLevel,
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
            isMicEnable = floatingAudio.isMicEnabled,
            cameraEnabled = floatingVideo.isVideoEnable,
            placeholder = {
                DefaultVideoPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.SurfaceDark)
                        .padding(8.dp),
                    audioLevel = floatingMedia.mediaUser.audioStream.audioLevel,
                    participant = floatingMedia.participant,
                    displayNameTextStyle = Typography.displaySmall.copy(fontSize = 20.sp),
                    languageTextStyle = Typography.bodyLarge.copy(fontSize = 12.sp),
                    languageIconSize = 12.dp,
                    maxBlurValue = 24.dp,
                )
            }
        )
    }
}

@Composable
private fun PipModeScreenType(
    callUserUiModelList: ImmutableList<CallUserUiModel>,
    pipModeScreenType: CallingScreenType.PipMode,
) {
    val firstPipMedia = callUserUiModelList.first { it.mediaKey == pipModeScreenType.pipFirstMediaKey }
    val secondPipMedia = callUserUiModelList.firstOrNull { it.mediaKey == pipModeScreenType.pipSecondMediaKey }
    val firstVideo = firstPipMedia.mediaUser.videoStream
    val firstAudio = firstPipMedia.mediaUser.audioStream
    val secondVideo = secondPipMedia?.mediaUser?.videoStream
    val secondAudio = secondPipMedia?.mediaUser?.audioStream
    val isFirstFrontCamera = firstVideo is LocalVideoStream && firstVideo.isFrontCamera
    val isSecondFrontCamera = secondVideo is LocalVideoStream && secondVideo.isFrontCamera

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Colors.Black)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NormalVideo(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .conditional(firstVideo.videoTrack != null && firstVideo.isVideoEnable) {
                    speakingGlow(
                        audioLevel = firstAudio.audioLevel,
                        shape = RoundedCornerShape(16.dp),
                        minBlurDp = 1.dp,
                        maxBlurDp = 30.dp,
                        outlineWidth = 1.dp,
                        outlineAlpha = 0.7f
                    )
                }
                .clip(RoundedCornerShape(16.dp)),
            videoTrack = firstVideo.videoTrack,
            isFrontCamera = isFirstFrontCamera,
            isMicEnable = firstAudio.isMicEnabled,
            cameraEnabled = firstVideo.isVideoEnable,
            placeholder = {
                DefaultVideoPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.SurfaceDark)
                        .padding(24.dp),
                    audioLevel = firstAudio.audioLevel,
                    participant = firstPipMedia.participant,
                    displayNameTextStyle = Typography.displayMedium.copy(fontSize = 28.sp),
                    languageTextStyle = Typography.bodyLarge.copy(fontSize = 16.sp),
                    languageIconSize = 24.dp,
                )
            }
        )

        if (secondPipMedia != null) {
            NormalVideo(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .conditional(secondVideo?.videoTrack != null && secondVideo.isVideoEnable) {
                        speakingGlow(
                            audioLevel = secondAudio?.audioLevel ?: 0f,
                            shape = RoundedCornerShape(16.dp),
                            minBlurDp = 1.dp,
                            maxBlurDp = 30.dp,
                            outlineWidth = 1.dp,
                            outlineAlpha = 0.7f
                        )
                    }
                    .clip(RoundedCornerShape(16.dp)),
                videoTrack = secondVideo?.videoTrack,
                isFrontCamera = isSecondFrontCamera,
                isMicEnable = secondAudio?.isMicEnabled ?: false,
                cameraEnabled = secondVideo?.isVideoEnable ?: false,
                placeholder = {
                    DefaultVideoPlaceHolder(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Colors.SurfaceDark)
                            .padding(24.dp),
                        audioLevel = secondAudio?.audioLevel ?: 0f,
                        participant = secondPipMedia.participant,
                        displayNameTextStyle = Typography.displayMedium.copy(fontSize = 28.sp),
                        languageTextStyle = Typography.bodyLarge.copy(fontSize = 16.sp),
                        languageIconSize = 24.dp,
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun WaitingScreenPreviewMirror() {
    WaitingScreenPreview()
}

@Composable
@Preview
private fun CallingScreenFloatingTypePreviewMirror() {
    CallingScreenFloatingPreview()
}

@Composable
@Preview
private fun CallingScreenLazyGridTypePreviewMirror() {
    CallingScreenLazyGridPreview()
}

@Composable
@Preview
private fun CallingScreenGridTypePreviewMirror() {
    CallingScreenGridPreview()
}

@Composable
@Preview
private fun CallingScreenPipModePreviewMirror() {
    CallingScreenPipModePreview()
}