package com.youhajun.feature.auth.util

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.youhajun.feature.auth.api.GoogleAuthManager
import com.youhajun.feature.auth.BuildConfig
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GoogleAuthManagerImpl @Inject constructor(
    @param:ActivityContext private val context: Context,
) : GoogleAuthManager {

    private val credentialManager = CredentialManager.create(context)

    override suspend fun signIn(nonce: String): Result<String> = runCatching {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(nonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(request = request, context = context)
        val idToken = handleSignIn(result)
        idToken ?: throw Exception("Failed to find Google ID Token")
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        val clearRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(clearRequest)
    }

    private fun handleSignIn(result: GetCredentialResponse): String? {
        val credential = result.credential
        return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            googleIdTokenCredential.idToken
        } else null
    }
}

val LocalGoogleAuthManager = staticCompositionLocalOf<GoogleAuthManager> {
    error("No GoogleAuthManager provided")
}