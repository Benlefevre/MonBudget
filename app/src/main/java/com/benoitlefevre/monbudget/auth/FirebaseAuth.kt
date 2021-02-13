package com.benoitlefevre.monbudget.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class FirebaseAuth(val auth: FirebaseAuth, private val authUI: AuthUiWrapper) : Auth {

    private var _authState = MutableStateFlow(getCurrentState())

    override val authState: StateFlow<AuthState>
        get() = _authState

    override fun signIn(): Intent {
        return authUI.getAuthIntent()
    }

    override fun logOut() {
        auth.signOut()
        _authState.value = getCurrentState()
    }

    override fun isLogged() {
        _authState.value = getCurrentState()
    }

    private fun getCurrentState(): AuthState {
        val currentUser = auth.currentUser
        Timber.i("$currentUser")
        return if (currentUser == null) {
            AuthState.NotAuthenticated
        } else {
            AuthState.Authenticated(
                CurrentUserAuth(currentUser)
            )
        }
    }
}

class CurrentUserAuth(private val firebaseUser: FirebaseUser) : CurrentUser {
    override val id: String
        get() = firebaseUser.uid

    override val name: String
        get() = firebaseUser.displayName ?: "No name"

    override val mail: String
        get() = firebaseUser.email ?: "No mail"
}