package com.youhajun.feature.impl

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.core.permission.PermissionHandler
import com.youhajun.core.permission.rememberPermissionRequestController
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.api.LocalCallIntentFactory
import com.youhajun.hyanghae.graphics.modifier.conditional
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.bottomBorder
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.components.room.RoomVisibilityRow
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun CreateRoomRoute(
    viewModel: CreateRoomViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionController = rememberPermissionRequestController()
    val callIntentFactory = LocalCallIntentFactory.current

    viewModel.collectSideEffect {
        when (it) {
            is CreateRoomSideEffect.Navigation -> onNavigate(it.navigationEvent)
            is CreateRoomSideEffect.GoToCall -> {
                val callIntent = callIntentFactory.getCallActivityIntent(context, it.roomId)
                context.startActivity(callIntent)
            }

            CreateRoomSideEffect.PermissionCheck -> {
                permissionController.request()
            }
        }
    }

    PermissionHandler(
        controller = permissionController,
        permissions = persistentListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ),
        rationaleMessage = stringResource(R.string.permission_rationale_message_camera_and_mic),
        onPermissionGranted = viewModel::onPermissionGranted
    )

    CreateRoomScreen(
        tagTextField = viewModel.tagInput,
        titleTextField = viewModel.titleInput,
        state = state,
        onClickBack = viewModel::onClickBack,
        onTitleInputTextChanged = viewModel::onTitleInputTextChanged,
        onTagInputTextChanged = viewModel::onTagInputTextChanged,
        onClickParticipantCount = viewModel::onClickParticipantCount,
        onClickVisibility = viewModel::onClickVisibility,
        onTagInsert = viewModel::onTagInsert,
        onTagDelete = viewModel::onTagDelete,
        onClickCreateRoom = viewModel::onClickCreateRoom,
    )
}

@Composable
internal fun CreateRoomScreen(
    state: CreateRoomState,
    titleTextField: TextFieldValue,
    tagTextField: TextFieldValue,
    onClickBack: () -> Unit,
    onTitleInputTextChanged: (TextFieldValue) -> Unit,
    onTagInputTextChanged: (TextFieldValue) -> Unit,
    onClickParticipantCount: (Int) -> Unit,
    onClickVisibility: (RoomVisibility) -> Unit,
    onTagInsert: () -> Unit,
    onTagDelete: (String) -> Unit,
    onClickCreateRoom: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White),
    ) {
        CreateRoomHeader(onClickBack)

        CreateRoomBody(
            titleTextField = titleTextField,
            tagTextField = tagTextField,
            maxParticipantCount = state.maxParticipantCount,
            selectedMaxParticipantCount = state.selectedMaxParticipantCount,
            selectVisibility = state.selectedRoomVisibility,
            tags = state.tags,
            maxTagCount = state.maxTagCount,
            onTitleInputTextChanged = onTitleInputTextChanged,
            onTagInputTextChanged = onTagInputTextChanged,
            onClickParticipantCount = onClickParticipantCount,
            onClickVisibility = onClickVisibility,
            onTagInsert = onTagInsert,
            onTagDelete = onTagDelete,
            onClickCreateRoom = onClickCreateRoom,
        )
    }
}

@Composable
private fun CreateRoomHeader(
    onClickBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .bottomBorder(1.dp, Colors.FFEBEBEA)
            .padding(vertical = 12.dp, horizontal = 8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            tint = Colors.Black,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterStart)
                .noRippleClickable { onClickBack() }
        )

        Text(
            text = stringResource(R.string.create_room_header_title),
            color = Colors.Black,
            style = Typography.titleMedium,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun ColumnScope.CreateRoomBody(
    titleTextField: TextFieldValue,
    tagTextField: TextFieldValue,
    maxParticipantCount: Int,
    selectedMaxParticipantCount: Int,
    selectVisibility: RoomVisibility,
    tags: ImmutableSet<String>,
    maxTagCount: Int,
    onTitleInputTextChanged: (TextFieldValue) -> Unit,
    onTagInputTextChanged: (TextFieldValue) -> Unit,
    onClickParticipantCount: (Int) -> Unit,
    onClickVisibility: (RoomVisibility) -> Unit,
    onTagInsert: () -> Unit,
    onTagDelete: (String) -> Unit,
    onClickCreateRoom: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        VerticalSpacer(28.dp)

        TitleSection(
            titleTextField = titleTextField,
            onTitleInputTextChanged = onTitleInputTextChanged,
        )

        VerticalSpacer(32.dp)

        ParticipantCountSection(
            maxParticipantCount = maxParticipantCount,
            selectedMaxParticipantCount = selectedMaxParticipantCount,
            onClickParticipantCount = onClickParticipantCount,
        )

        VerticalSpacer(32.dp)

        VisibilitySection(
            selectVisibility = selectVisibility,
            onClickVisibility = onClickVisibility
        )

        VerticalSpacer(32.dp)

        TagSection(
            tags = tags,
            tagTextField = tagTextField,
            maxTagCount = maxTagCount,
            onTagInputTextChanged = onTagInputTextChanged,
            onTagInsert = onTagInsert,
            onTagDelete = onTagDelete
        )

        Spacer(modifier = Modifier.weight(1f))

        CreateButton(onClickCreateRoom = onClickCreateRoom)

        VerticalSpacer(24.dp)
    }
}

@Composable
private fun TitleSection(
    titleTextField: TextFieldValue,
    onTitleInputTextChanged: (TextFieldValue) -> Unit = {},
) {
    Text(
        text = stringResource(R.string.create_room_title_text),
        style = Typography.bodyMedium,
        fontWeight = FontWeight.W600,
        color = Colors.Black,
    )

    VerticalSpacer(8.dp)

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        value = titleTextField,
        onValueChange = onTitleInputTextChanged,
        textStyle = TextStyle(
            color = Colors.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
    ) { innerTextField ->
        val isTitleEmpty = titleTextField.text.isEmpty()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = if (isTitleEmpty) Colors.FFEBEBEA else Colors.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            innerTextField()

            if (titleTextField.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.create_room_title_hint),
                    color = Colors.FF8C8D8B,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }
}

@Composable
private fun ParticipantCountSection(
    maxParticipantCount: Int,
    selectedMaxParticipantCount: Int,
    onClickParticipantCount: (Int) -> Unit,
) {
    Text(
        text = stringResource(R.string.create_room_max_participant_count_text),
        style = Typography.bodyMedium,
        fontWeight = FontWeight.W600,
        color = Colors.Black,
    )

    VerticalSpacer(8.dp)

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(maxParticipantCount) { idx ->
            val count = idx + 1
            if (count <= 1) return@repeat

            val isSelected = count == selectedMaxParticipantCount
            val backgroundColor = if (isSelected) Colors.FFFF5724 else Colors.White
            val textColor = if (isSelected) Colors.White else Colors.Black

            key(count) {
                Box(
                    modifier = Modifier
                        .conditional(!isSelected) {
                            Modifier.border(
                                width = 1.dp,
                                color = Colors.FFEBEBEA,
                                RoundedCornerShape(8.dp)
                            )
                        }
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                        .noRippleClickable { onClickParticipantCount(count) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        color = textColor,
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.W600),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun VisibilitySection(
    selectVisibility: RoomVisibility,
    onClickVisibility: (RoomVisibility) -> Unit,
) {
    Text(
        text = stringResource(R.string.create_room_visibility_text),
        style = Typography.bodyMedium,
        fontWeight = FontWeight.W600,
        color = Colors.Black,
    )

    VerticalSpacer(8.dp)

    RoomVisibilityRow(
        selectedRoomVisibility = selectVisibility,
        onClick = onClickVisibility,
    )
}

@Composable
private fun TagSection(
    tags: ImmutableSet<String>,
    tagTextField: TextFieldValue,
    maxTagCount: Int,
    onTagInputTextChanged: (TextFieldValue) -> Unit,
    onTagInsert: () -> Unit,
    onTagDelete: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.create_room_tag_text, maxTagCount),
        style = Typography.bodyMedium,
        fontWeight = FontWeight.W600,
        color = Colors.Black,
    )

    VerticalSpacer(8.dp)

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        value = tagTextField,
        onValueChange = onTagInputTextChanged,
        textStyle = TextStyle(
            color = Colors.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onTagInsert() }
        ),
        singleLine = true,
    ) { innerTextField ->
        val isTitleEmpty = tagTextField.text.isEmpty()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = if (isTitleEmpty) Colors.FFEBEBEA else Colors.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            innerTextField()

            if (tagTextField.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.create_room_tag_hint),
                    color = Colors.FF8C8D8B,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }

    VerticalSpacer(12.dp)

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {
            key(it) {
                Box(
                    modifier = Modifier
                        .background(Colors.FFFF5724, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .noRippleClickable { onTagDelete(it) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        color = Colors.White,
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.W600),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateButton(
    onClickCreateRoom: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.FFFF5724, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
            .noRippleClickable { onClickCreateRoom() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.create_room_create_button_text),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.W600, fontSize = 16.sp),
            color = Colors.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun CreateRoomPreview() {
    CreateRoomScreen(
        state = CreateRoomState(
            maxParticipantCount = 8,
            selectedMaxParticipantCount = 2,
            selectedRoomVisibility = RoomVisibility.PUBLIC,
            tags = persistentSetOf("Tag1", "Tag2"),
            maxTagCount = 8,
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