package com.youhajun.feature.call.calling

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.conversation.ConversationState
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.feature.call.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaKey
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalMediaUser
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.RemoteAudioStream
import com.youhajun.webrtc.model.RemoteMediaUser
import com.youhajun.webrtc.model.RemoteVideoStream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

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
            participantsMap = getDummyParticipants(1),
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.Waiting,
            callMediaUsers = getDummyCallMediaUsers(1)
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
internal fun CallingScreenFloatingPreview() {
    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participantsMap = getDummyParticipants(2),
            recentConversation = TranslationMessage(
                conversationId = "abc",
                roomId = "",
                senderId = "1",
                state = ConversationState.PENDING,
                originText = "Hello",
                originLanguage = LanguageType.ENGLISH,
                transText = "안녕",
                transLanguage = LanguageType.KOREAN,
                createdAtToEpochTime = System.currentTimeMillis(),
                updatedAtToEpochTime = System.currentTimeMillis()
            ),
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.FloatingAndFull(
                floatingMediaKey = CallMediaKey.createKey("1", MediaContentType.DEFAULT.type),
                fullMediaKey = CallMediaKey.createKey("2", MediaContentType.DEFAULT.type),
            ),
            callMediaUsers = getDummyCallMediaUsers(2)
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
internal fun CallingScreenLazyGridPreview() {
    val callMediaUserSize = 5

    val callMediaUserList = getDummyCallMediaUsers(callMediaUserSize)
    val participantsMap = getDummyParticipants(callMediaUserSize)

    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participantsMap = participantsMap,
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.Grid,
            callMediaUsers = callMediaUserList,
            recentConversation = TranslationMessage(
                conversationId = "abc",
                roomId = "",
                state = ConversationState.PENDING,
                senderId = "1",
                originText = "Hello",
                originLanguage = LanguageType.ENGLISH,
                transText = "안녕",
                transLanguage = LanguageType.KOREAN,
                createdAtToEpochTime = System.currentTimeMillis(),
                updatedAtToEpochTime = System.currentTimeMillis()
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
internal fun CallingScreenGridPreview() {
    val callMediaUserSize = 4
    val callMediaUserList = getDummyCallMediaUsers(callMediaUserSize)
    val participantsMap = getDummyParticipants(callMediaUserSize)

    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participantsMap = participantsMap,
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.Grid,
            callMediaUsers = callMediaUserList,
            recentConversation = TranslationMessage(
                conversationId = "abc",
                roomId = "",
                senderId = "1",
                state = ConversationState.PENDING,
                originText = "Hello",
                originLanguage = LanguageType.ENGLISH,
                transText = "안녕",
                transLanguage = LanguageType.KOREAN,
                createdAtToEpochTime = System.currentTimeMillis(),
                updatedAtToEpochTime = System.currentTimeMillis()
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
internal fun CallingScreenPipModePreview() {
    CallingScreen(
        state = CallingState(
            myUserId = "1",
            roomInfo = RoomInfo(
                roomId = "1",
                roomCode = "123456",
                joinType = RoomJoinType.CODE_JOIN
            ),
            participantsMap = getDummyParticipants(2),
            isShowBottomCallController = true,
            callingScreenType = CallingScreenType.PipMode(
                popupScreenType = CallingScreenType.Grid,
                pipFirstMediaKey = CallMediaKey.createKey("1", MediaContentType.DEFAULT.type),
                pipSecondMediaKey = CallMediaKey.createKey("2", MediaContentType.DEFAULT.type),
            ),
            callMediaUsers = getDummyCallMediaUsers(2)
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

private fun getDummyCallMediaUsers(size: Int = 4): ImmutableList<CallMediaUser> {
    return (1..size).map { index ->
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
}

private fun getDummyParticipants(size: Int = 4): ImmutableMap<String, Participant> {
    return (1..size).associate { index ->
        val userId = index.toString()
        userId to Participant(
            participantId = userId,
            userId = userId,
            displayName = "User $userId",
            imageUrl = "",
            language = LanguageType.ENGLISH
        )
    }.toImmutableMap()
}