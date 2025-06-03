package com.youhajun.core.event

sealed interface MainEvent {
    data class ShowSnackBar(val message: String) : MainEvent

    data object RequireLogin : MainEvent
}