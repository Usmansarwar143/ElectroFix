package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.MessageThread
import com.example.electrofix.data.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagesUiState(
    val messageThreads: List<MessageThread> = emptyList()
)

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState

    init {
        loadMessageThreads()
    }

    private fun loadMessageThreads() {
        viewModelScope.launch {
            _uiState.value = MessagesUiState(messageThreads = messagesRepository.getMessageThreads())
        }
    }
}
