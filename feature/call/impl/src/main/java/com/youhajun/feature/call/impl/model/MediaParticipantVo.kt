package com.youhajun.feature.call.impl.model

import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.webrtc.model.CallMediaUser

data class CallUserUiModel(
    val currentParticipant: CurrentParticipant?,
    val mediaUser: CallMediaUser,
    val mediaKey: String = mediaUser.key,
    val userId: String = mediaUser.userId,
)