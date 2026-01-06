package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.ProfileRepository
import com.example.electrofix.data.UserProfile
import com.example.electrofix.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val isProfileSaved: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfileFlow().collect { userProfile ->
                _uiState.value = _uiState.value.copy(userProfile = userProfile)
            }
        }
    }

    fun saveProfile(name: String) {
        viewModelScope.launch {
            profileRepository.saveUserProfile(name)
            _uiState.value = _uiState.value.copy(isProfileSaved = true)
        }
    }

    fun updateProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            profileRepository.updateUserProfile(userProfile)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            // Clear any profile state if needed
            _uiState.value = ProfileUiState()
        }
    }
}
