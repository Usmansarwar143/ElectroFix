package com.example.electrofix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Represents a single message in the chat
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

// Represents the complete state for the AI Chatbot screen
data class ChatbotUiState(
    val messages: List<ChatMessage> = emptyList(),
    val currentInput: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    // Initialize Gemini Model
    // Using injected GenerativeModel which is configured in AppModule with API Key

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("System Prompt: You are the AI assistant for ElectroFix, a home appliance repair app. The CEO & Developer is Usman Sarwar, and the CTO & Developer is Abdul Moiz Barlas. Your goal is to help users find technicians, book repairs, and diagnose appliances. Be professional, helpful, and concise.") },
            content(role = "model") { text("Understood. I am the AI assistant for ElectroFix. The CEO & Developer is Usman Sarwar, and the CTO & Developer is Abdul Moiz Barlas. I will assist users with appliance repairs, bookings, and diagnostics professionally and concisely.") }
        )
    )

    init {
        // Add an initial greeting from the AI
        _uiState.update {
            it.copy(messages = listOf(ChatMessage("Hello! I am your AI Assistant from ElectroFix. How can I help you regarding your appliances today?", isFromUser = false)))
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(currentInput = text) }
    }

    fun sendMessage() {
        val userInput = _uiState.value.currentInput
        if (userInput.isBlank()) return

        // Add user's message to the UI immediately
        val userMessage = ChatMessage(userInput, isFromUser = true)
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                currentInput = "" // Clear input field
            )
        }

        // Show loading indicator and call the API
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Call Gemini API
                val response = chat.sendMessage(userInput)
                val aiResponseText = response.text ?: "I didn't get that. Could you try again?"

                // Add AI's response to the UI
                _uiState.update {
                    it.copy(messages = it.messages + ChatMessage(aiResponseText, isFromUser = false), isLoading = false)
                }
            } catch (e: Exception) {
                // Handle Error
                val errorText = "Sorry, I'm having trouble connecting right now. Please check your internet or API key."
                _uiState.update {
                    it.copy(messages = it.messages + ChatMessage(errorText, isFromUser = false), isLoading = false)
                }
                e.printStackTrace()
            }
        }
    }
}
