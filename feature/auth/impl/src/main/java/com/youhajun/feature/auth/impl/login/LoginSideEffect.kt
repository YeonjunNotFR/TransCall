package com.youhajun.feature.auth.impl.login

import com.youhajun.core.route.NavigationEvent

sealed class LoginSideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : LoginSideEffect()

    data class GoogleSignIn(val nonce: String) : LoginSideEffect()
}