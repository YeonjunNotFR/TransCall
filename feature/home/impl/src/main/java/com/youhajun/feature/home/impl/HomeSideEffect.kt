package com.youhajun.feature.home.impl

sealed class HomeSideEffect {

    sealed class Navigation: HomeSideEffect() {
        data object GoToCallHistory: Navigation()
        data class GoToCallWaiting(val roomCode: String): Navigation()
    }


}