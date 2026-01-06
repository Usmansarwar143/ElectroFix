package com.example.electrofix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.* 
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.electrofix.ui.components.AuthTextField
import com.example.electrofix.ui.components.GlassCard
// import com.example.electrofix.ui.components.GradientBackground // Removed GradientBackground
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.viewmodels.ChatMessage
import com.example.electrofix.viewmodels.ChatbotViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatbotScreen(
    navController: NavController,
    chatbotViewModel: ChatbotViewModel = hiltViewModel()
) {
    val chatbotState by chatbotViewModel.uiState.collectAsState()

    // Removed GradientBackground
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                // Adjusted colors to match default Scaffold background
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Use default background color
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val listState = rememberLazyListState()

            // Auto-scroll to the bottom
            LaunchedEffect(chatbotState.messages.size) {
                if (chatbotState.messages.isNotEmpty()) {
                    listState.animateScrollToItem(chatbotState.messages.size - 1)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(chatbotState.messages) { message ->
                    MessageBubble(message)
                }
                if (chatbotState.isLoading) {
                    item {
                        MessageBubble(ChatMessage("Typing...", isFromUser = false))
                    }
                }
            }

            MessageInput(chatbotViewModel)
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.isFromUser
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(shape)
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(text = message.text, color = textColor)
        }
    }
}

@Composable
private fun MessageInput(viewModel: ChatbotViewModel) {
    val state by viewModel.uiState.collectAsState()

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthTextField(
                value = state.currentInput,
                onValueChange = viewModel::onInputChange,
                label = "Ask me anything...",
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.sendMessage() },
                enabled = state.currentInput.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Message",
                    tint = if (state.currentInput.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}
