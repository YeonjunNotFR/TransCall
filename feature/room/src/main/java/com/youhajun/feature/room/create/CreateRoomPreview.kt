package com.youhajun.feature.room.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.room.RoomVisibility
import kotlinx.collections.immutable.persistentSetOf

@Preview
@Composable
internal fun CreateRoomPreview() {
    CreateRoomScreen(
        state = CreateRoomState(
            maxParticipantCount = 8,
            selectedMaxParticipantCount = 2,
            selectedRoomVisibility = RoomVisibility.PUBLIC,
            tags = persistentSetOf("Tag1", "Tag2"),
            maxTagCount = 8,
            titleHint = "안녕하세요^^"
        ),
        titleTextField = TextFieldValue("Room Title"),
        tagTextField = TextFieldValue("Tag1, Tag2"),
        onClickBack = {},
        onTitleInputTextChanged = {},
        onClickParticipantCount = {},
        onClickVisibility = {},
        onTagInputTextChanged = {},
        onTagInsert = {},
        onTagDelete = {},
        onClickCreateRoom = {}
    )
}