package com.youhajun.feature.call.impl.calling

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.SenderInfo
import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.LocalVideoStream
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
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN,
            ),
            participants = persistentListOf(
                CurrentParticipant(
                    userId = "1",
                    displayName = "User 1",
                    imageUrl = "",
                    language = LanguageType.ENGLISH
                ),
            ),
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.Waiting,
            callMediaUsers = persistentListOf(
                LocalMediaUser(
                    userId = "1",
                    mediaContentType = MediaContentType.DEFAULT.type,
                    videoStream = LocalVideoStream(
                        videoTrack = null,
                        isFrontCamera = true,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "1"
                    ),
                    audioStream = LocalAudioStream(
                        isMicEnabled = true,
                        audioLevel = 0.5f,
                        selectedDevice = AudioDeviceType.NONE,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "1",
                        audioTrack = null
                    )
                )
            )
        ),
        onTapCallingScreen = {},
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
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participants = persistentListOf(
                CurrentParticipant(
                    userId = "1",
                    displayName = "User 1",
                    imageUrl = "",
                    language = LanguageType.ENGLISH
                ),
            ),
            recentConversation = Conversation(
                id = "1",
                roomId = "123456",
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
            callingScreenType = CallingScreenType.FloatingAndFull(
                floatingCallUser = CallUserUiModel(
                    currentParticipant = CurrentParticipant(
                        userId = "1",
                        displayName = "User 1",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                    mediaUser = LocalMediaUser(
                        userId = "1",
                        mediaContentType = MediaContentType.DEFAULT.type,
                        videoStream = LocalVideoStream(
                            videoTrack = null,
                            isFrontCamera = true,
                            isVideoEnable = true,
                            mediaContentType = MediaContentType.DEFAULT.type,
                            userId = "1"
                        ),
                        audioStream = LocalAudioStream(
                            isMicEnabled = true,
                            audioLevel = 0.5f,
                            selectedDevice = AudioDeviceType.NONE,
                            mediaContentType = MediaContentType.DEFAULT.type,
                            userId = "1",
                            audioTrack = null
                        )
                    )
                ),
                fullCallUser = CallUserUiModel(
                    currentParticipant = CurrentParticipant(
                        userId = "2",
                        displayName = "User 2",
                        imageUrl = "",
                        language = LanguageType.ENGLISH
                    ),
                    mediaUser = RemoteMediaUser(
                        userId = "2",
                        mediaContentType = MediaContentType.DEFAULT.type,
                        videoStream = RemoteVideoStream(
                            videoTrack = null,
                            isVideoEnable = true,
                            mediaContentType = MediaContentType.DEFAULT.type,
                            userId = "2"
                        ),
                        audioStream = RemoteAudioStream(
                            isMicEnabled = true,
                            mediaContentType = MediaContentType.DEFAULT.type,
                            userId = "2",
                            audioTrack = null
                        )
                    )
                )
            ),
            callMediaUsers = persistentListOf(
                LocalMediaUser(
                    userId = "1",
                    mediaContentType = MediaContentType.DEFAULT.type,
                    videoStream = LocalVideoStream(
                        videoTrack = null,
                        isFrontCamera = true,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "1"
                    ),
                    audioStream = LocalAudioStream(
                        isMicEnabled = true,
                        audioLevel = 0.5f,
                        selectedDevice = AudioDeviceType.NONE,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "1",
                        audioTrack = null
                    )
                ),
                RemoteMediaUser(
                    userId = "2",
                    mediaContentType = MediaContentType.DEFAULT.type,
                    videoStream = RemoteVideoStream(
                        videoTrack = null,
                        isVideoEnable = true,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "2"
                    ),
                    audioStream = RemoteAudioStream(
                        isMicEnabled = true,
                        mediaContentType = MediaContentType.DEFAULT.type,
                        userId = "2",
                        audioTrack = null
                    )
                )
            ),
        ),
        onTapCallingScreen = {},
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
                mediaContentType = MediaContentType.DEFAULT.type,
                videoStream = LocalVideoStream(
                    videoTrack = null,
                    isFrontCamera = true,
                    isVideoEnable = true,
                    mediaContentType = MediaContentType.DEFAULT.type,
                    userId = userId
                ),
                audioStream = LocalAudioStream(
                    isMicEnabled = true,
                    audioLevel = 0.5f,
                    selectedDevice = AudioDeviceType.NONE,
                    mediaContentType = MediaContentType.DEFAULT.type,
                    userId = userId,
                    audioTrack = null
                )
            )
        } else {
            RemoteMediaUser(
                userId = userId,
                mediaContentType = MediaContentType.DEFAULT.type,
                videoStream = RemoteVideoStream(
                    videoTrack = null,
                    isVideoEnable = true,
                    mediaContentType = MediaContentType.DEFAULT.type,
                    userId = userId
                ),
                audioStream = RemoteAudioStream(
                    isMicEnabled = true,
                    mediaContentType = MediaContentType.DEFAULT.type,
                    userId = userId,
                    audioTrack = null
                )
            )
        }
    }.toImmutableList()

    val currentParticipantList = (1..callMediaUserSize).map { index ->
        CurrentParticipant(
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
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participants = currentParticipantList,
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.Grid,
            callMediaUsers = callMediaUserList,
            recentConversation = Conversation(
                id = "1",
                roomId = "123456",
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
        onTapCallingScreen = {},
        onDoubleTapFloating = {},
        onDoubleTapFull = {},
        onDoubleTapGrid = {},
        onClickCallAction = {},
        onClickRoomCodeCopy = {},
        onClickRoomCodeShare = {},
    )
}