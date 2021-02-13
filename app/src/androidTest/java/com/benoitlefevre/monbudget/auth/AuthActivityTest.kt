package com.benoitlefevre.monbudget.auth

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.benoitlefevre.monbudget.MainActivity
import com.benoitlefevre.monbudget.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module


@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var module: Module

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        module = module(override = true) {
            single<Auth> {
                FirebaseAuth(
                    firebaseAuth,
                    AuthUiWrapper()
                )
            }
            viewModel { AuthViewModel(get()) }
        }
        loadKoinModules(module)
    }

    @After
    fun tearDown() {
        unmockkAll()
        unloadKoinModules(module)
    }

    @Test
    fun authActivityUiTest() {
        every { firebaseAuth.currentUser } returns null
        ActivityScenario.launch(AuthActivity::class.java)
        assertDisplayed(R.id.welcome,R.string.welcome_in_mon_budget)
        assertDisplayed(R.id.appTitle,R.string.app_name)
        assertDisplayed(R.id.auth_logo)
        assertDisplayed(R.id.google_btn)
        assertDisplayed(R.id.mail_btn)
        assertDisplayed(R.id.anonymous_btn)
    }

    @Test
    fun startMainActivityWhenUserIsLogged() {
        Intents.init()
        every { firebaseUser.uid } returns "13rCoWthFVQ2LEZ5x5vuzlTCNGs2"
        every { firebaseAuth.currentUser } returns firebaseUser
        ActivityScenario.launch(AuthActivity::class.java)
        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
    }
}

