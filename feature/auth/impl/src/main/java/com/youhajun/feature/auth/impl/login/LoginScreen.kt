package com.youhajun.feature.auth.impl.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.auth.impl.util.LocalGoogleAuthManager
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.util.noRippleClickable
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val googleAuthManager = LocalGoogleAuthManager.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is LoginSideEffect.Navigation -> onNavigate(it.navigationEvent)
            is LoginSideEffect.GoogleSignIn -> {
                val idToken = googleAuthManager.signIn(it.nonce)
                viewModel.onGoogleLoginResult(idToken)
            }
        }
    }

    LoginScreen(
        onClickGoogleLogin = viewModel::onClickGoogleLogin,
    )
}

@Composable
private fun LoginScreen(
    onClickGoogleLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.FFF6F8FA)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "App Logo",
            tint = Colors.PrimaryLight,
            modifier = Modifier.size(100.dp)
        )

        VerticalSpacer(16.dp)

        Text(
            text = stringResource(R.string.app_name),
            style = Typography.displayLarge.copy(
                fontSize = 64.sp,
                fontWeight = FontWeight.W500
            ),
            color = Colors.PrimaryLight
        )

        VerticalSpacer(52.dp)

        Text(
            text = stringResource(R.string.login_description_app),
            style = Typography.displaySmall,
            color = Colors.Black,
            textAlign = TextAlign.Center
        )

        VerticalSpacer(110.dp)

        GoogleSignInButton(onClickGoogleLogin = onClickGoogleLogin)

    }
}

@Composable
private fun GoogleSignInButton(
    onClickGoogleLogin: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .background(Colors.White, shape = RoundedCornerShape(8.dp))
            .noRippleClickable(onClick = onClickGoogleLogin)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )

        HorizontalSpacer(8.dp)

        Text(
            text = stringResource(R.string.login_google_login),
            color = Colors.Black,
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.W600,
            )
        )
    }
}

@Preview
@Composable
internal fun LoginPreview() {
    LoginScreen { }
}

@Preview
@Composable
private fun LoginPreviewMirror() {
    LoginPreview()
}