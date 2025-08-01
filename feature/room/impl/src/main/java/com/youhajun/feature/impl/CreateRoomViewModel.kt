package com.youhajun.feature.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.room.usecase.CreateRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableSet
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createRoomUseCase: CreateRoomUseCase,
) : ContainerHost<CreateRoomState, CreateRoomSideEffect>, ViewModel() {

    companion object {
        private const val MAX_PARTICIPANT_COUNT = 8
        private const val MAX_TAG_COUNT = 8
    }

    var titleInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    var tagInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    override val container: Container<CreateRoomState, CreateRoomSideEffect> = container(CreateRoomState(
        maxParticipantCount = MAX_PARTICIPANT_COUNT,
        maxTagCount = MAX_TAG_COUNT,
    )) {
        onInit()
    }

    fun onClickBack() {
        intent {
            postSideEffect(CreateRoomSideEffect.Navigation(NavigationEvent.NavigateBack))
        }
    }

    fun onTitleInputTextChanged(text: TextFieldValue) {
        titleInput = text
    }

    fun onTagInputTextChanged(text: TextFieldValue) {
        tagInput = text
    }

    fun onClickParticipantCount(count: Int) {
        intent {
            reduce { state.copy(selectedMaxParticipantCount = count) }
        }
    }

    fun onClickVisibility(roomVisibility: RoomVisibility) {
        intent {
            reduce { state.copy(selectedRoomVisibility = roomVisibility) }
        }
    }

    fun onTagInsert() {
        intent {
            if (tagInput.text.isBlank() || MAX_TAG_COUNT >= state.tags.size) return@intent

            val newTags = state.tags.toMutableSet().apply {
                add(tagInput.text)
            }.toImmutableSet()
            reduce { state.copy(tags = newTags) }
            tagInput = TextFieldValue("")
        }
    }

    fun onTagDelete(tag: String) {
        intent {
            val newTags = state.tags.toMutableSet().apply {
                remove(tag)
            }.toImmutableSet()
            reduce { state.copy(tags = newTags) }
        }
    }

    fun onClickCreateRoom() {

    }

    private fun onInit() {

    }
}
