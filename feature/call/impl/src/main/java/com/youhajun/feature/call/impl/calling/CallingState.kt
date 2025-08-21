package com.youhajun.feature.call.impl.calling

import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.feature.call.impl.model.CallControlAction
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaKey
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalVideoStream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class CallingState(
    val myUserId: String = "",
    val roomInfo: RoomInfo = RoomInfo(""),
    val participants: ImmutableList<CurrentParticipant> = persistentListOf(),
    val isShowBottomCallController: Boolean = true,
    val callMediaUsers: ImmutableList<CallMediaUser> = persistentListOf(),
    val callingScreenType: CallingScreenType = CallingScreenType.Waiting,
    val isShowAudioDeviceChangeDialog: Boolean = false,
    val recentConversation: Conversation? = null
) {
    private val participantMap = participants.associateBy { it.userId }

    val callUserUiModelList: ImmutableList<CallUserUiModel> = callMediaUsers.map { mediaUser ->
        CallUserUiModel(
            currentParticipant = participantMap[mediaUser.userId],
            mediaUser = mediaUser,
        )
    }.toImmutableList()


    val myDefaultCallUser: CallUserUiModel? = callUserUiModelList.firstOrNull { it.mediaKey == CallMediaKey.createKey(myUserId, MediaContentType.DEFAULT.type) }
    val myAudioStream: LocalAudioStream? = myDefaultCallUser?.mediaUser?.audioStream as? LocalAudioStream
    val myVideoStream: LocalVideoStream? = myDefaultCallUser?.mediaUser?.videoStream as? LocalVideoStream

    val callControlActionList: ImmutableList<CallControlAction> = persistentListOf(
        CallControlAction.ToggleMicMute(myAudioStream?.isMicEnabled ?: true),
        CallControlAction.SelectAudioDevice(myAudioStream?.selectedDevice ?: AudioDeviceType.NONE),
        CallControlAction.CallingLeft,
        CallControlAction.FlipCamera(myVideoStream?.isFrontCamera ?: true),
        CallControlAction.ToggleCameraEnable(myVideoStream?.isVideoEnable ?: true),
    )
}