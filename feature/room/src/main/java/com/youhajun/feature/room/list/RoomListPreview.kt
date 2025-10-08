package com.youhajun.feature.room.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.SortDirection
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomJoinType
import com.youhajun.core.model.room.RoomStatus
import com.youhajun.core.model.room.RoomVisibility
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Preview
@Composable
internal fun RoomListPreview() {
    RoomListScreen(
        state = RoomListState(
            currentRoomList = getDummyCurrentRoomInfoList(10),
            participantSort = SortDirection.ASC,
            createdAtSort = SortDirection.DESC
        ),
        roomLazyListState = rememberLazyListState(),
        onClickRoom = {},
        onClickCreateRoom = {},
        onChangeCreatedAtSort = {},
        onChangeParticipantCountSort = {}
    )
}
private fun getDummyCurrentRoomInfoList(size: Int): ImmutableList<CurrentRoomInfo> {
    val roomList = getDummyRoomInfoList(size)
    return roomList.map { room ->
        val participants = getDummyParticipants()
        CurrentRoomInfo(
            roomInfo = room,
            currentParticipants = participants.values.toImmutableList()
        )
    }.toImmutableList()
}
private fun getDummyParticipants(size: Int = (1..10).random()): ImmutableMap<String, Participant> {
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

private fun getDummyRoomInfoList(size: Int): ImmutableList<RoomInfo> =
    (1..size).map { i ->
        RoomInfo(
            roomId = "room-$i",
            roomCode = "CODE%03d".format(i),
            title = "Room $i",
            maxParticipantCount = 4 + (i % 5),
            currentParticipantCount = (i % 4),
            visibility = if (i % 2 == 0) RoomVisibility.PUBLIC else RoomVisibility.PRIVATE,
            joinType = RoomJoinType.CODE_JOIN,
            tags = persistentSetOf("tag${i%3}", "tag${(i+1)%3}"),
            status = when (i % 3) {
                0 -> RoomStatus.WAITING
                1 -> RoomStatus.IN_PROGRESS
                else -> RoomStatus.ENDED
            }
        )
    }.toImmutableList()