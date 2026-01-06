package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.repository.AuthRepository
import com.example.electrofix.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserState(
    val isLoading: Boolean = true,
    val user: FirebaseUser? = null,
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val isPremium: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserState())
    val uiState: StateFlow<UserState> = _uiState.asStateFlow()

    private val _editName = MutableStateFlow("")
    val editName: StateFlow<String> = _editName.asStateFlow()

    private val _editPhone = MutableStateFlow("")
    val editPhone: StateFlow<String> = _editPhone.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                _uiState.value = _uiState.value.copy(user = currentUser, email = currentUser.email ?: "")
                try {
                    val userData = userRepository.getUserData(currentUser.uid)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        name = userData?.name ?: "",
                        role = userData?.role ?: "Customer",
                        isPremium = userData?.isPremium ?: false
                    )
                    _editName.value = userData?.name ?: ""
                    _editPhone.value = userData?.phone ?: ""
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onNameChange(name: String) {
        _editName.value = name
    }

    fun onPhoneChange(phone: String) {
        _editPhone.value = phone
    }

    fun saveProfileChanges() {
        viewModelScope.launch {
            _uiState.value.user?.let {
                try {
                    userRepository.updateUserData(it.uid, _editName.value, _editPhone.value)
                    _uiState.value = _uiState.value.copy(name = _editName.value)
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            }
        }
    }

    fun upgradeToPremium() {
        viewModelScope.launch {
            _uiState.value.user?.let {
                try {
                    userRepository.updatePremiumStatus(it.uid, true)
                    _uiState.value = _uiState.value.copy(isPremium = true)
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
