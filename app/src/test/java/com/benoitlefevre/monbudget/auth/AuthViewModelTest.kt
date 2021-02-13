package com.benoitlefevre.monbudget.auth

import android.app.Activity
import android.content.Intent
import com.benoitlefevre.monbudget.R
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var firebaseUser: FirebaseUser
    @MockK
    private lateinit var idpResponse: IdpResponse
    private lateinit var viewModel: AuthViewModel
    private lateinit var stateFlow: MutableStateFlow<AuthState>

    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        every { firebaseUser.uid } returns "1"
        every { firebaseUser.displayName } returns "test"
        every { firebaseUser.email } returns "test@test.fr"
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun returnedAuthenticatedState_whenAuthReturnedAuthenticated() = runBlocking {
        stateFlow = MutableStateFlow(AuthState.Authenticated(CurrentUserAuth(firebaseUser)))
        every { firebaseAuth.authState } returns stateFlow
        viewModel = AuthViewModel(firebaseAuth)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.Authenticated)
    }

    @Test
    fun returnedNotAuthenticatedState_whenAuthReturnedNotAuthenticated() = runBlocking {
        stateFlow = MutableStateFlow(AuthState.NotAuthenticated)
        val intent = Intent()
        every { firebaseAuth.authState } returns stateFlow
        every { firebaseAuth.signIn() } returns intent
        viewModel = AuthViewModel(firebaseAuth)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.NeedAuthentication(intent))
    }

    @Test
    fun handleActivityResult_failureIdpResponse_AuthUiErrorReturned() = runBlocking{
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_OK,null)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.ErrorAuthentication(R.string.there_is_an_error))
    }

    @Test
    fun handleActivityResult_success_AuthUIAuthenticatedReturned() = runBlocking{
        every { idpResponse.providerType } returns GoogleAuthProvider.PROVIDER_ID
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_OK,idpResponse)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.Authenticated)
    }

    @Test
    fun handleActivityResult_failureResultCode_AuthUiErrorReturned() = runBlocking{
        every { idpResponse.providerType } returns GoogleAuthProvider.PROVIDER_ID
        every { idpResponse.error } returns null
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_CANCELED,idpResponse)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.ErrorAuthentication(R.string.retry_login))
    }

    @Test
    fun handleActivityResult_error_AuthUiNetworkErrorReturned() = runBlocking {
        every { idpResponse.providerType } returns null
        every { idpResponse.error?.errorCode } returns ErrorCodes.NO_NETWORK
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_OK,idpResponse)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.ErrorAuthentication(R.string.network_login))
    }

    @Test
    fun handleActivityResult_error_AuthUiUnknownErrorReturned() = runBlocking {
        every { idpResponse.providerType } returns null
        every { idpResponse.error?.errorCode } returns ErrorCodes.UNKNOWN_ERROR
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_OK,idpResponse)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.ErrorAuthentication(R.string.unknown_error))
    }

    @Test
    fun handleActivityResult_error_AuthUiOtherErrorReturned() = runBlocking {
        every { idpResponse.providerType } returns null
        every { idpResponse.error?.errorCode } returns ErrorCodes.DEVELOPER_ERROR
        viewModel = AuthViewModel(firebaseAuth)
        viewModel.handleResponse(Activity.RESULT_OK,idpResponse)
        val result = viewModel.state.first()
        assertThat(result).isEqualTo(AuthUiState.ErrorAuthentication(R.string.retry_login))
    }
}