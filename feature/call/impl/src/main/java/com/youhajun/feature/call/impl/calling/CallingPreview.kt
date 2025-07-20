package com.youhajun.feature.call.impl.calling

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomType
import com.youhajun.core.model.calling.StageMessageType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.SenderInfo
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.RemoteAudioStream
import com.youhajun.webrtc.model.RemoteMediaUser
import com.youhajun.webrtc.model.RemoteVideoStream
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


@Preview
@Composable
internal fun WaitingScreenPreview() {
    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                code = "123456",
                participants = persistentListOf(
                    Participant(
                        userId = "1",
                        displayName = "User 1",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                ),
                roomType = RoomType.CODE_JOIN
            ),
            isShowBottomCallController = true,
            stageMessageType = StageMessageType.Waiting,
            callingScreenType = CallingScreenType.Grid,
            callMediaUsers = persistentListOf(
                LocalMediaUser(
                    userId = "1",
                    mediaContentType = MediaContentType.CAMERA,
                    videoStream = LocalVideoStream(
                        videoTrack = null,
                        isFrontCamera = true,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "1"
                    ),
                    audioStream = LocalAudioStream(
                        isMicEnabled = true,
                        selectedDevice = AudioDeviceType.NONE,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "1",
                        audioTrack = null
                    )
                )
            )
        ),
        onTabCallingScreen = {},
        onDoubleTapFloating = {},
        onDoubleTapFull = {},
        onDoubleTapGrid = {},
        onClickCallAction = {},
        onClickRoomCodeCopy = {},
        onClickRoomCodeShare = {},
    )
}


@Preview
@Composable
internal fun CallingScreenFloatingType() {
    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                code = "123456",
                participants = persistentListOf(
                    Participant(
                        userId = "1",
                        displayName = "User 1",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                    Participant(
                        userId = "2",
                        displayName = "User 2",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    )
                ),
                roomType = RoomType.CODE_JOIN
            ),
            recentConversation = Conversation(
                id = "1",
                roomCode = "123456",
                senderInfo = SenderInfo(
                    senderId = "1",
                    displayName = "User 1",
                    profileUrl = null,
                    language = LanguageType.ENGLISH
                ),
                originText = "Hello",
                transText = "Hello",
                transLanguage = LanguageType.KOREAN,
                timestamp = System.currentTimeMillis()
            ),
            isShowBottomCallController = true,
            stageMessageType = StageMessageType.Calling,
            callingScreenType = CallingScreenType.FloatingAndFull(
                floatingCallUser = CallUserUiModel(
                    participant = Participant(
                        userId = "1",
                        displayName = "User 1",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                    mediaUser = LocalMediaUser(
                        userId = "1",
                        mediaContentType = MediaContentType.CAMERA,
                        videoStream = LocalVideoStream(
                            videoTrack = null,
                            isFrontCamera = true,
                            isVideoEnable = true,
                            mediaContentType = MediaContentType.CAMERA,
                            userId = "1"
                        ),
                        audioStream = LocalAudioStream(
                            isMicEnabled = true,
                            selectedDevice = AudioDeviceType.NONE,
                            mediaContentType = MediaContentType.CAMERA,
                            userId = "1",
                            audioTrack = null
                        )
                    )
                ),
                fullCallUser = CallUserUiModel(
                    participant = Participant(
                        userId = "2",
                        displayName = "User 2",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                    mediaUser = RemoteMediaUser(
                        userId = "2",
                        mediaContentType = MediaContentType.CAMERA,
                        videoStream = RemoteVideoStream(
                            videoTrack = null,
                            isVideoEnable = true,
                            mediaContentType = MediaContentType.CAMERA,
                            userId = "2"
                        ),
                        audioStream = RemoteAudioStream(
                            isMicEnabled = true,
                            mediaContentType = MediaContentType.CAMERA,
                            userId = "2",
                            audioTrack = null
                        )
                    )
                )
            ),
            callMediaUsers = persistentListOf(
                LocalMediaUser(
                    userId = "1",
                    mediaContentType = MediaContentType.CAMERA,
                    videoStream = LocalVideoStream(
                        videoTrack = null,
                        isFrontCamera = true,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "1"
                    ),
                    audioStream = LocalAudioStream(
                        isMicEnabled = true,
                        selectedDevice = AudioDeviceType.NONE,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "1",
                        audioTrack = null
                    )
                ),
                RemoteMediaUser(
                    userId = "2",
                    mediaContentType = MediaContentType.CAMERA,
                    videoStream = RemoteVideoStream(
                        videoTrack = null,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "2"
                    ),
                    audioStream = RemoteAudioStream(
                        isMicEnabled = true,
                        mediaContentType = MediaContentType.CAMERA,
                        userId = "2",
                        audioTrack = null
                    )
                )
            ),
        ),
        onTabCallingScreen = {},
        onDoubleTapFloating = {},
        onDoubleTapFull = {},
        onDoubleTapGrid = {},
        onClickCallAction = {},
        onClickRoomCodeCopy = {},
        onClickRoomCodeShare = {},
    )
}

@Preview
@Composable
internal fun CallingScreenGridType() {
    val callMediaUserSize = 4
    val callMediaUserList = (1..callMediaUserSize).map { index ->
        val userId = index.toString()
        if (index == 1) {
            LocalMediaUser(
                userId = userId,
                mediaContentType = MediaContentType.CAMERA,
                videoStream = LocalVideoStream(
                    videoTrack = null,
                    isFrontCamera = true,
                    isVideoEnable = true,
                    mediaContentType = MediaContentType.CAMERA,
                    userId = userId
                ),
                audioStream = LocalAudioStream(
                    isMicEnabled = true,
                    selectedDevice = AudioDeviceType.NONE,
                    mediaContentType = MediaContentType.CAMERA,
                    userId = userId,
                    audioTrack = null
                )
            )
        } else {
            RemoteMediaUser(
                userId = userId,
                mediaContentType = MediaContentType.CAMERA,
                videoStream = RemoteVideoStream(
                    videoTrack = null,
                    isVideoEnable = true,
                    mediaContentType = MediaContentType.CAMERA,
                    userId = userId
                ),
                audioStream = RemoteAudioStream(
                    isMicEnabled = true,
                    mediaContentType = MediaContentType.CAMERA,
                    userId = userId,
                    audioTrack = null
                )
            )
        }
    }.toImmutableList()

    val participantList = (1..callMediaUserSize).map { index ->
        Participant(
            userId = index.toString(),
            displayName = "User $index",
            imageUrl = "",
            language = LanguageType.ENGLISH
        )
    }.toImmutableList()

    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                code = "123456",
                participants = participantList,
                roomType = RoomType.CODE_JOIN
            ),
            isShowBottomCallController = true,
            stageMessageType = StageMessageType.Calling,
            callingScreenType = CallingScreenType.Grid,
            callMediaUsers = callMediaUserList,
            recentConversation = Conversation(
                id = "1",
                roomCode = "123456",
                senderInfo = SenderInfo(
                    senderId = "1",
                    displayName = "User 1",
                    profileUrl = null,
                    language = LanguageType.ENGLISH
                ),
                originText = "Hello",
                transText = "Hello",
                transLanguage = LanguageType.KOREAN,
                timestamp = System.currentTimeMillis()
            )
        ),
        onTabCallingScreen = {},
        onDoubleTapFloating = {},
        onDoubleTapFull = {},
        onDoubleTapGrid = {},
        onClickCallAction = {},
        onClickRoomCodeCopy = {},
        onClickRoomCodeShare = {},
    )
}