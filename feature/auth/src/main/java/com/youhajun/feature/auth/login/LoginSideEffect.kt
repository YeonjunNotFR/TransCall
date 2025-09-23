package com.youhajun.feature.auth.login

import com.youhajun.core.model.auth.Nonce
import com.youhajun.core.route.NavigationEvent

sealed class LoginSideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : LoginSideEffect()

    data class GoogleSignIn(val nonce: Nonce) : LoginSideEffect()
}