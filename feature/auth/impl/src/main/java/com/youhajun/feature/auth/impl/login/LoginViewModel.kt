package com.youhajun.feature.auth.impl.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.model.auth.SocialLoginRequest
import com.youhajun.core.model.auth.SocialType
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.auth.usecase.GetLoginNonceUseCase
import com.youhajun.domain.auth.usecase.SocialLoginUseCase
import com.youhajun.feature.home.api.HomeNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginNonceUseCase: GetLoginNonceUseCase,
    private val socialLoginUseCase: SocialLoginUseCase
) : ContainerHost<LoginState, LoginSideEffect>, ViewModel() {

    override val container: Container<LoginState, LoginSideEffect> = container(LoginState())

    fun onClickGoogleLogin() {
        viewModelScope.launch {
            getLoginNonceUseCase().onSuccess { nonce ->
                intent {
                    postSideEffect(LoginSideEffect.GoogleSignIn(nonce))
                }
            }
        }
    }

    fun onGoogleLoginResult(loginRequestId: String, result: Result<String>) {
        result.onSuccess { idToken ->
            val request = SocialLoginRequest(loginRequestId, SocialType.GOOGLE, idToken)
            socialLogin(request)
        }
    }

    private fun socialLogin(request: SocialLoginRequest) {
        viewModelScope.launch {
            socialLoginUseCase(request).onSuccess {
                intent {
                    val event = NavigationEvent.NavigateAndClear(HomeNavRoute.Home, true)
                    postSideEffect(LoginSideEffect.Navigation(event))
                }
            }
        }
    }
}