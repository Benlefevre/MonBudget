package com.benoitlefevre.monbudget.auth

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benoitlefevre.monbudget.R
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class AuthUiState() {
    data class NeedAuthentication(val intent: Intent) : AuthUiState()
    data class ErrorAuthentication(val message: Int) : AuthUiState()
    object Authenticated : AuthUiState()
    object Loading : AuthUiState()
}

class AuthViewModel(private val auth: Auth) : ViewModel() {

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val state: StateFlow<AuthUiState> get() = _state

    init {
        viewModelScope.launch {
            auth.authState.collect {
                when (it) {
                    AuthState.NotAuthenticated -> {
                        Timber.i("NotAuthenticated")
                        val intent = auth.signIn()
                        _state.value = AuthUiState.NeedAuthentication(intent)
                    }
                    is AuthState.Authenticated -> {
                        Timber.i("Authenticated")
                        _state.value = AuthUiState.Authenticated
                    }
                }
            }
        }
    }

    fun handleResponse(resultCode: Int, idpResponse: IdpResponse?) {
        if (resultCode == Activity.RESULT_OK && idpResponse?.providerType != null) {
            _state.value = AuthUiState.Authenticated
        } else {
            if (idpResponse == null) {
                _state.value = AuthUiState.ErrorAuthentication(R.string.there_is_an_error)
            } else {
                _state.value = when (idpResponse.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        AuthUiState.ErrorAuthentication(R.string.network_login)
                    ErrorCodes.UNKNOWN_ERROR ->
                        AuthUiState.ErrorAuthentication(R.string.unknown_error)
                    else ->
                        AuthUiState.ErrorAuthentication(R.string.retry_login)
                }
            }
        }
        auth.isLogged()
    }
}