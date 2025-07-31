package com.youhajun.feature.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.domain.room.usecase.CreateRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createRoomUseCase: CreateRoomUseCase,
) : ContainerHost<CreateRoomState, CreateRoomSideEffect>, ViewModel() {

    companion object {
        private const val MAX_PARTICIPANT_COUNT = 8
    }

    var titleInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    var tagInput: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    override val container: Container<CreateRoomState, CreateRoomSideEffect> = container(CreateRoomState(maxParticipantCount = MAX_PARTICIPANT_COUNT)) {
        onInit()
    }

    fun onClickBack() {

    }

    fun onTitleInputTextChanged(text: TextFieldValue) {
        titleInput = text
    }

    fun onTagInputTextChanged(text: TextFieldValue) {
        tagInput = text
    }

    fun onClickParticipantCount(count: Int) {

    }

    fun onClickVisibility(roomVisibility: RoomVisibility) {

    }

    fun onTagInsert() {

    }

    fun onTagDelete(tag: String) {

    }

    fun onClickCreateRoom() {

    }

    private fun onInit() {

    }
}
