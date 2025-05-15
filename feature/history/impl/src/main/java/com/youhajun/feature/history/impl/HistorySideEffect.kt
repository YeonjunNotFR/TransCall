package com.youhajun.feature.history.impl

sealed class HistorySideEffect {

    sealed class Navigation: HistorySideEffect() {
        data class GoToCallWaiting(val roomCode: String): Navigation()
    }
}