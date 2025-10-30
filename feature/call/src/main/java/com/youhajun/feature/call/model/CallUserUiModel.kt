package com.youhajun.feature.call.model

import com.youhajun.core.model.room.Participant
import com.youhajun.webrtc.model.stream.CallMediaUser

data class CallUserUiModel(
    val participant: Participant?,
    val mediaUser: CallMediaUser,
    val mediaKey: String = mediaUser.key,
    val userId: String = mediaUser.userId,
)