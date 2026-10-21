package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.User
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class AuthUiState(
    val isSignUp: Boolean = false,
    val name: String = "",
    val email: String = "shivam.singh@memo.demo",
    val pass: String = "demo1234",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isFirstTimeUser: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val statusMessage: String? = null
)

class AuthViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCurrentUser().collect { user ->
                _uiState.value = _uiState.value.copy(user = user)
            }
        }
    }

    fun isProfileCompleted(): Boolean {
        return repository.isProfileCompleted() || (_uiState.value.user?.isProfileCompleted == true)
    }

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
                    val isFirstTime = !repository.isProfileCompleted() && !user.isProfileCompleted
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        isFirstTimeUser = isFirstTime,
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
                        isFirstTimeUser = true,
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
        viewModelScope.launch {
            val demoUser = repository.getCurrentUser().firstOrNull() ?: DemoDataSource.currentUser
            val isFirstTime = !repository.isProfileCompleted() && !demoUser.isProfileCompleted
            _uiState.value = _uiState.value.copy(
                email = demoUser.email,
                pass = "demo1234",
                isLoading = false,
                isLoggedIn = true,
                isFirstTimeUser = isFirstTime,
                user = demoUser,
                statusMessage = "Signed in with Offline Demo profile"
            )
        }
    }

    fun updateProfile(
        name: String,
        nickname: String,
        bloodGroup: String,
        age: Int,
        gender: String,
        emergencyContact: String = "",
        allergies: String = "",
        onComplete: (() -> Unit)? = null
    ) {
        val currentUser = _uiState.value.user ?: DemoDataSource.currentUser
        val newUser = currentUser.copy(
            name = name.trim().ifBlank { currentUser.name },
            nickname = nickname.trim(),
            bloodGroup = bloodGroup.trim().ifBlank { currentUser.bloodGroup },
            age = if (age > 0) age else currentUser.age,
            gender = gender.trim().ifBlank { currentUser.gender },
            emergencyContact = emergencyContact.trim(),
            allergies = allergies.trim(),
            isProfileCompleted = true
        )
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, statusMessage = null)
        viewModelScope.launch {
            val result = repository.updateUserProfile(newUser)
            result.fold(
                onSuccess = { savedUser ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isFirstTimeUser = false,
                        user = savedUser,
                        statusMessage = "Profile updated successfully!"
                    )
                    onComplete?.invoke()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to update profile"
                    )
                }
            )
        }
    }

    fun seedSampleData() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null, statusMessage = null)
        viewModelScope.launch {
            try {
                for (report in DemoDataSource.reports) {
                    repository.uploadReport(report.title, 1024L)
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusMessage = "Successfully seeded 12 sample reports to Firestore!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to seed sample data"
                )
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }
}
