package com.benoitlefevre.monbudget.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benoitlefevre.monbudget.MainActivity
import com.firebase.ui.auth.IdpResponse
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()
    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val idpResponse = IdpResponse.fromResultIntent(it.data)
            viewModel.handleResponse(it.resultCode, idpResponse)
        }
        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { uiState ->
                when (uiState) {
                    AuthUiState.Loading -> {
                        Timber.i("Loading")
                    }
                    is AuthUiState.NeedAuthentication -> {
                        Timber.i("NeedAuthentication")
                        content.launch(uiState.intent)
                    }
                    is AuthUiState.ErrorAuthentication -> {
                        Timber.i("ErrorAuthentication")
                        Toast.makeText(this@AuthActivity,uiState.message,Toast.LENGTH_LONG).show()
                    }
                    is AuthUiState.Authenticated -> {
                        Timber.i("Authenticated")
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        this@AuthActivity.finish()
                    }
                }
            }
        }
    }
}