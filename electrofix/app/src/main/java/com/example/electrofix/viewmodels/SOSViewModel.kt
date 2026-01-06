package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SOSViewModel @Inject constructor() : ViewModel() {

    private var pressCount = 0
    private var firstPressTimestamp = 0L

    private val _sosTriggered = MutableSharedFlow<Unit>()
    val sosTriggered = _sosTriggered.asSharedFlow()

    fun onSosButtonPressed() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            if (pressCount == 0) {
                // First press
                pressCount = 1
                firstPressTimestamp = currentTime
            } else {
                // Subsequent presses
                if (currentTime - firstPressTimestamp <= 3000) {
                    pressCount++
                    if (pressCount == 5) {
                        sendSOSAlert()
                        resetSOS()
                    }
                } else {
                    // Timeout, reset
                    resetSOS()
                    // Start a new sequence with the current press
                    pressCount = 1
                    firstPressTimestamp = currentTime
                }
            }
        }
    }

    private suspend fun sendSOSAlert() {
        // In a real app, this would trigger a call to a backend service.
        // For now, we just emit an event to be collected by the UI.
        _sosTriggered.emit(Unit)
    }

    private fun resetSOS() {
        pressCount = 0
        firstPressTimestamp = 0L
    }
}
