package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.AuthRepository
import com.example.electrofix.data.ProfileRepository
import com.example.electrofix.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                android.util.Log.d("SplashCheck", "User: $user")
                if (user != null) {
                    val profile = profileRepository.getUserProfileFlow().firstOrNull()
                    android.util.Log.d("SplashCheck", "Profile: $profile")
                    if (profile != null && profile.name.isNotBlank()) {
                        _startDestination.value = Routes.DASHBOARD_HOST
                    } else {
                        _startDestination.value = Routes.CREATE_PROFILE
                    }
                } else {
                    android.util.Log.d("SplashCheck", "User is null, going to Role")
                    _startDestination.value = Routes.ROLE
                }
            } catch (e: Exception) {
                // In case of any error, fallback to the role selection
                android.util.Log.e("SplashCheck", "Error checking status", e)
                _startDestination.value = Routes.ROLE
            }
        }
    }
}