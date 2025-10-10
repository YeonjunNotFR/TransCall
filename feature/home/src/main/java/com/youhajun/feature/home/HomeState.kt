package com.youhajun.feature.home

import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.user.MyInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val myInfo: MyInfo = MyInfo(),
    val callHistoryList: ImmutableList<CallHistory> = persistentListOf(),
    val callHistoryPreviewMaxSize: Int,
    val isShowJoinBottomSheet: Boolean = false,
    val currentRoomInfo: CurrentRoomInfo? = null
)