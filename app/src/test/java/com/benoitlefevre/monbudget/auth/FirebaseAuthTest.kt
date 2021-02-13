package com.benoitlefevre.monbudget.auth

import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FirebaseAuthTest {

    @MockK
    private lateinit var firebaseAuth: com.google.firebase.auth.FirebaseAuth
    @MockK
    private lateinit var authUI : AuthUiWrapper

    @MockK
    private lateinit var firebaseUser: FirebaseUser

    @MockK
    private lateinit var task : Task<Void>
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { firebaseUser.uid } returns "1"
        every { firebaseUser.displayName } returns "test"
        every { firebaseUser.email } returns "test@test.fr"
    }

    @Test
    fun initAuth_success_authStateAuthenticatedReturned() = runBlocking {
        every { firebaseAuth.currentUser } returns firebaseUser
        auth = FirebaseAuth(firebaseAuth,authUI)
        val result = auth.authState.first()
        assertThat(result).isInstanceOf(AuthState.Authenticated::class.java)
    }

    @Test
    fun initAuthWithNullValue_success_CorrectUserReturned() = runBlocking {
        every { firebaseUser.uid } returns "1"
        every { firebaseUser.displayName } returns null
        every { firebaseUser.email } returns null
        every { firebaseAuth.currentUser } returns firebaseUser
        val result = CurrentUserAuth(firebaseUser)
        assertThat(result.id).isEqualTo("1")
        assertThat(result.name).isEqualTo("No name")
        assertThat(result.mail).isEqualTo("No mail")
    }

    @Test
    fun initAuthWithCorrectValue_success_CorrectUserReturned() = runBlocking {
        every { firebaseAuth.currentUser } returns firebaseUser
        val result = CurrentUserAuth(firebaseUser)
        assertThat(result.id).isEqualTo("1")
        assertThat(result.name).isEqualTo("test")
        assertThat(result.mail).isEqualTo("test@test.fr")
    }

    @Test
    fun initAuth_failure_authStateNotAuthenticatedReturned() = runBlocking {
        every { firebaseAuth.currentUser } returns null
        auth = FirebaseAuth(firebaseAuth, authUI)
        val result = auth.authState.first()
        assertThat(result).isInstanceOf(AuthState.NotAuthenticated::class.java)
    }

    @Test
    fun logOut_success_authStateNotAuthenticatedReturned() = runBlocking {
        every { firebaseAuth.signOut() } returns Unit
        every { firebaseAuth.currentUser } returns null
        auth = FirebaseAuth(firebaseAuth,authUI)
        auth.logOut()
        assertThat(auth.authState.first()).isInstanceOf(AuthState.NotAuthenticated::class.java)
    }

    @Test
    fun isLogged_success_authStateAuthenticatedReturned() = runBlocking {
        every { firebaseAuth.currentUser } returns firebaseUser
        auth = FirebaseAuth(firebaseAuth,authUI)
        auth.isLogged()
        val result = auth.authState.first()
        assertThat(result).isInstanceOf(AuthState.Authenticated::class.java)
    }

    @Test
    fun isLogged_failure_authStateNotAuthenticatedReturned() = runBlocking {
        every { firebaseAuth.currentUser } returns null
        auth = FirebaseAuth(firebaseAuth,authUI)
        auth.isLogged()
        val result = auth.authState.first()
        assertThat(result).isInstanceOf(AuthState.NotAuthenticated::class.java)
    }
}