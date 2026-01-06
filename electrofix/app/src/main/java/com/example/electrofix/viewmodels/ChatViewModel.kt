package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.models.ChatMessage
import com.example.electrofix.models.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val inputText = _uiState.value.inputText.trim()
        if (inputText.isNotEmpty()) {
            val userMessage = ChatMessage(inputText, true)
            val currentMessages = _uiState.value.messages.toMutableList()
            currentMessages.add(userMessage)

            _uiState.value = _uiState.value.copy(
                messages = currentMessages,
                inputText = ""
            )

            viewModelScope.launch {
                // Dummy AI response
                val aiResponse = ChatMessage("This is a dummy AI response.", false)
                val updatedMessages = _uiState.value.messages.toMutableList()
                updatedMessages.add(aiResponse)
                _uiState.value = _uiState.value.copy(messages = updatedMessages)
            }
        }
    }
}
