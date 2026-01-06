package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.AuthRepository
import com.example.electrofix.data.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: FirebaseUser, val isNewUser: Boolean) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

enum class AuthScreenState {
    LOGIN,
    SIGN_UP,
    FORGOT_PASSWORD
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _authScreenState = MutableStateFlow(AuthScreenState.LOGIN)
    val authScreenState: StateFlow<AuthScreenState> = _authScreenState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _selectedRole = MutableStateFlow("Customer")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun isUserLoggedIn(): Boolean = authRepository.getCurrentUser() != null

    fun onEmailChange(email: String) { _email.value = email }
    fun onPasswordChange(password: String) { _password.value = password }
    fun onConfirmPasswordChange(password: String) { _confirmPassword.value = password }
    fun onRoleSelected(role: String) { _selectedRole.value = role }

    fun showLogin() { _authScreenState.value = AuthScreenState.LOGIN }
    fun showSignUp() { _authScreenState.value = AuthScreenState.SIGN_UP }
    fun showForgotPassword() { _authScreenState.value = AuthScreenState.FORGOT_PASSWORD }

    fun handleSignUp() {
        viewModelScope.launch {
            if (_password.value != _confirmPassword.value) {
                _uiState.value = AuthUiState.Error("Passwords do not match.")
                return@launch
            }
            _uiState.value = AuthUiState.Loading
            try {
                kotlinx.coroutines.withTimeout(15000L) {
                    android.util.Log.d("AuthViewModel", "Starting SignUp: ${_email.value}")
                    val authResult = authRepository.signUp(_email.value, _password.value)
                    val user = authResult.user!!
                    android.util.Log.d("AuthViewModel", "Auth successful. User UID: ${user.uid}. Creating Firestore document...")
                    userRepository.createUser(user.uid, user.email!!, _selectedRole.value)
                    android.util.Log.d("AuthViewModel", "Firestore document created.")
                    _uiState.value = AuthUiState.Success(user, isNewUser = true)
                }
            } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                android.util.Log.e("AuthViewModel", "SignUp Timeout", e)
                _uiState.value = AuthUiState.Error("Signup timed out. Please check your internet connection.")
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "SignUp Error", e)
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun handleLogin() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val authResult = authRepository.login(_email.value, _password.value)
                _uiState.value = AuthUiState.Success(authResult.user!!, isNewUser = false)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun handlePasswordReset() {
        viewModelScope.launch {
            if (_email.value.isBlank()) {
                _uiState.value = AuthUiState.Error("Please enter your email address.")
                return@launch
            }
            _uiState.value = AuthUiState.Loading
            try {
                authRepository.sendPasswordResetEmail(_email.value)
                _uiState.value = AuthUiState.Error("Password reset instructions have been sent to your email.")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Failed to send password reset email.")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
