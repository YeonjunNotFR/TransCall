package com.youhajun.feature.call.impl.calling

import com.youhajun.core.model.calling.StageMessageType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.feature.call.impl.model.CallControlAction
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class CallingState(
    val myUserId: String = "",
    val roomInfo: RoomInfo = RoomInfo(""),
    val isShowBottomCallController: Boolean = true,
    val stageMessageType: StageMessageType = StageMessageType.Waiting,
    val callMediaUsers: ImmutableList<CallMediaUser> = persistentListOf(),
    val callingScreenType: CallingScreenType = CallingScreenType.Grid,
    val isShowAudioDeviceChangeDialog: Boolean = false,
    val recentConversation: Conversation? = null
) {
    private val participantMap = roomInfo.participants.associateBy { it.userId }

    val callUserUiModelList: ImmutableList<CallUserUiModel> = callMediaUsers.map { mediaUser ->
        CallUserUiModel(
            participant = participantMap[mediaUser.userId],
            mediaUser = mediaUser,
        )
    }.toImmutableList()


    val myCameraCallUser: CallUserUiModel? = callUserUiModelList.firstOrNull { it.mediaKey == myUserId + MediaContentType.CAMERA }
    val myAudioStream: LocalAudioStream? = myCameraCallUser?.mediaUser?.audioStream as? LocalAudioStream
    val myVideoStream: LocalVideoStream? = myCameraCallUser?.mediaUser?.videoStream as? LocalVideoStream

    val callControlActionList: ImmutableList<CallControlAction> = persistentListOf(
        CallControlAction.ToggleMicMute(myAudioStream?.isMicEnabled ?: true),
        CallControlAction.SelectAudioDevice(myAudioStream?.selectedDevice ?: AudioDeviceType.NONE),
        CallControlAction.CallingLeft,
        CallControlAction.FlipCamera(myVideoStream?.isFrontCamera ?: true),
        CallControlAction.ToggleCameraEnable(myVideoStream?.isVideoEnable ?: true),
    )
}