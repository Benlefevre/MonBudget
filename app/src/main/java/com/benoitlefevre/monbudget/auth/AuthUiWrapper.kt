package com.benoitlefevre.monbudget.auth

import android.content.Intent
import com.benoitlefevre.monbudget.R
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI

class AuthUiWrapper {
    private val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.AnonymousBuilder().build()
    )

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAuthMethodPickerLayout(
                AuthMethodPickerLayout
                    .Builder(R.layout.activity_auth)
                    .setEmailButtonId(R.id.mail_btn)
                    .setGoogleButtonId(R.id.google_btn)
                    .setAnonymousButtonId(R.id.anonymous_btn)
                    .build()
            )
            .setTheme(R.style.AuthTheme)
            .enableAnonymousUsersAutoUpgrade()
            .setIsSmartLockEnabled(false)
            .setAlwaysShowSignInMethodScreen(true)
            .build()
    }
}