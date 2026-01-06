package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import com.example.electrofix.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        authRepository.signOut()
    }
}
