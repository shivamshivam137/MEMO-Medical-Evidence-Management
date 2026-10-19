package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.User
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isSignUp: Boolean = false,
    val name: String = "",
    val email: String = "shivam.singh@memo.demo",
    val pass: String = "demo1234",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val statusMessage: String? = null
)

class AuthViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun toggleAuthMode() {
        val current = _uiState.value
        _uiState.value = current.copy(
            isSignUp = !current.isSignUp,
            errorMessage = null,
            statusMessage = null
        )
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name, errorMessage = null)
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChanged(pass: String) {
        _uiState.value = _uiState.value.copy(pass = pass, errorMessage = null)
    }

    fun login() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null, statusMessage = null)
        viewModelScope.launch {
            val result = repository.login(state.email, state.pass)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user,
                        statusMessage = "Authenticated successfully"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Authentication failed"
                    )
                }
            )
        }
    }

    fun signUp() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null, statusMessage = null)
        viewModelScope.launch {
            val result = repository.signUp(state.email, state.pass, state.name)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user,
                        statusMessage = "Account registered successfully"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Registration failed"
                    )
                }
            )
        }
    }

    fun loginWithDemo() {
        val demoUser = DemoDataSource.currentUser
        _uiState.value = _uiState.value.copy(
            email = demoUser.email,
            pass = "demo1234",
            isLoading = false,
            isLoggedIn = true,
            user = demoUser,
            statusMessage = "Signed in with Offline Demo profile"
        )
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }
}
