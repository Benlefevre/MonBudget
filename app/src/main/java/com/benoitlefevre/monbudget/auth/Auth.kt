package com.benoitlefevre.monbudget.auth

import android.content.Intent
import kotlinx.coroutines.flow.StateFlow

interface Auth {
    val authState: StateFlow<AuthState>
    fun logOut()
    fun signIn(): Intent
    fun isLogged()
}

sealed class AuthState() {
    object NotAuthenticated : AuthState()
    data class Authenticated(val user: CurrentUser) : AuthState()
}

interface CurrentUser {
    val id: String
    val name: String
    val mail: String
}