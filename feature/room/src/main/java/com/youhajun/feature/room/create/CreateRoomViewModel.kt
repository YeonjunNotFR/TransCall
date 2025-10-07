package com.youhajun.feature.room.create

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.youhajun.core.design.R
import com.youhajun.core.model.room.CreateRoomRequest
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.room.usecase.CreateRoomUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableSet
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val createRoomUseCase: CreateRoomUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : ContainerHost<CreateRoomState, CreateRoomSideEffect>, ViewModel() {

    companion object {
        private const val MAX_TITLE_LENGTH = 20
        private const val MAX_TAG_LENGTH = 10
        private const val MAX_PARTICIPANT_COUNT = 8
        private const val MAX_TAG_COUNT = 8
    }

    var titleInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    var tagInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    override val container: Container<CreateRoomState, CreateRoomSideEffect> = container(
        CreateRoomState(
            maxParticipantCount = MAX_PARTICIPANT_COUNT,
            maxTagCount = MAX_TAG_COUNT
        )
    ) { getMyInfo() }

    fun onPermissionGranted() = intent {
        val request = getCreateRoomRequest()
        createRoomUseCase(request).onSuccess { roomId ->
            postSideEffect(CreateRoomSideEffect.GoToCall(roomId))
            postSideEffect(CreateRoomSideEffect.Navigation(NavigationEvent.NavigateBack))
        }
    }

    fun onClickBack() = intent {
        postSideEffect(CreateRoomSideEffect.Navigation(NavigationEvent.NavigateBack))
    }

    fun onTitleInputTextChanged(text: TextFieldValue) {
        if (text.text.length > MAX_TITLE_LENGTH) return
        titleInput = text
    }

    fun onTagInputTextChanged(text: TextFieldValue) {
        if (text.text.length > MAX_TAG_LENGTH) return
        tagInput = text
    }

    fun onClickParticipantCount(count: Int) = intent {
        reduce { state.copy(selectedMaxParticipantCount = count) }
    }

    fun onClickVisibility(roomVisibility: RoomVisibility) = intent {
        reduce { state.copy(selectedRoomVisibility = roomVisibility) }
    }

    fun onTagInsert() = intent {
        if (tagInput.text.isBlank() || MAX_TAG_COUNT <= state.tags.size) return@intent

        val newTags = state.tags.toMutableSet().apply { add(tagInput.text) }.toImmutableSet()
        reduce { state.copy(tags = newTags) }
        tagInput = TextFieldValue("")
    }

    fun onTagDelete(tag: String) = intent {
        val newTags = state.tags.toMutableSet().apply { remove(tag) }.toImmutableSet()
        reduce { state.copy(tags = newTags) }
    }

    fun onClickCreateRoom() = intent {
        postSideEffect(CreateRoomSideEffect.PermissionCheck)
    }

    private fun getCreateRoomRequest(): CreateRoomRequest = container.stateFlow.value.run {
        val title = titleInput.text.ifBlank { titleHint }
        CreateRoomRequest(
            title = title,
            maxParticipantCount = selectedMaxParticipantCount,
            visibility = selectedRoomVisibility,
            tags = tags,
        )
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getMyInfo() = subIntent {
        val name = getMyInfoUseCase().getOrNull()?.displayName
        val titleHint = if (name != null) context.getString(
            R.string.create_room_title_hint_with_name,
            name
        ) else context.getString(R.string.create_room_title_hint)
        reduce { state.copy(titleHint = titleHint) }
    }
}
