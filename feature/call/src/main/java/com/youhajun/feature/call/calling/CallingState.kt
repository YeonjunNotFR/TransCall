package com.youhajun.feature.call.calling

import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.feature.call.model.CallControlAction
import com.youhajun.feature.call.model.CallUserUiModel
import com.youhajun.feature.call.model.CallingScreenType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaKey
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.LocalAudioStream
import com.youhajun.webrtc.model.LocalVideoStream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList

data class CallingState(
    val myUserId: String = "",
    val roomInfo: RoomInfo = RoomInfo(""),
    val participantsMap: ImmutableMap<String, Participant> = persistentMapOf(),
    val isShowBottomCallController: Boolean = true,
    val callMediaUsers: ImmutableList<CallMediaUser> = persistentListOf(),
    val callingScreenType: CallingScreenType = CallingScreenType.Waiting,
    val isShowAudioDeviceChangeDialog: Boolean = false,
    val recentConversation: TranslationMessage? = null
) {
    val recentParticipant: Participant? = recentConversation?.let { participantsMap[it.senderId] }
    val shouldAutoEnterPip = callingScreenType is CallingScreenType.FloatingAndFull || callingScreenType is CallingScreenType.Grid

    val callUserUiModelList: ImmutableList<CallUserUiModel> = callMediaUsers.map { mediaUser ->
        CallUserUiModel(
            participant = participantsMap[mediaUser.userId],
            mediaUser = mediaUser,
        )
    }.toImmutableList()

    val myDefaultCallUser: CallUserUiModel? = callUserUiModelList.firstOrNull { it.mediaKey == CallMediaKey.createKey(myUserId, MediaContentType.DEFAULT.type) }
    val myAudioStream: LocalAudioStream? = myDefaultCallUser?.mediaUser?.audioStream as? LocalAudioStream
    val myVideoStream: LocalVideoStream? = myDefaultCallUser?.mediaUser?.videoStream as? LocalVideoStream

    val callControlActionList: ImmutableList<CallControlAction> = persistentListOf(
        CallControlAction.ToggleMicEnable(myAudioStream?.isMicEnabled ?: true),
        CallControlAction.SelectAudioDevice(myAudioStream?.selectedDevice ?: AudioDeviceType.NONE),
        CallControlAction.LeaveCall,
        CallControlAction.FlipCamera(myVideoStream?.isFrontCamera ?: true),
        CallControlAction.ToggleCameraEnable(myVideoStream?.isVideoEnable ?: true),
    )
}