package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.AuthRepository
import com.example.electrofix.data.UserRepository
import com.example.electrofix.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val user: User) : MainUiState
    data class Error(val message: String) : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            val firebaseUser = authRepository.getCurrentUser()
            if (firebaseUser != null) {
                try {
                    val user = userRepository.getUserData(firebaseUser.uid)
                    if (user != null) {
                        _uiState.value = MainUiState.Success(user)
                    } else {
                        _uiState.value = MainUiState.Error("User data not found")
                    }
                } catch (e: Exception) {
                    _uiState.value = MainUiState.Error(e.message ?: "Failed to fetch user data")
                }
            } else {
                _uiState.value = MainUiState.Error("User not logged in")
            }
        }
    }
}
