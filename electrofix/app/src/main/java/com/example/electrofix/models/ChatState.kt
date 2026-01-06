package com.example.electrofix.models

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = ""
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)
