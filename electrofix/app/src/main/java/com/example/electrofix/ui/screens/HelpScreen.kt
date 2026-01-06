package com.example.electrofix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope // Explicitly import ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width // Explicitly import width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help Center") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HelpSection("Getting Started") {
                HelpItem("How do I book a service?", "You can book a service by selecting a category from the home screen and filling out the booking form.")
                HelpItem("How do I contact a technician?", "You can message technicians directly through the Messages tab in the app.")
                HelpItem("What payment methods are accepted?", "We accept all major credit cards and digital payment methods.")
            }

            HelpSection("Account & Profile") {
                HelpItem("How do I update my profile?", "Go to Profile > Edit Profile to update your information.")
                HelpItem("How do I change my password?", "Go to Settings > Change Password to update your password.")
                HelpItem("How do I delete my account?", "Contact support through the app to request account deletion.")
            }

            HelpSection("Services") {
                HelpItem("What services do you offer?", "We offer appliance repair, electrical work, electronics repair, and smart home setup.")
                HelpItem("How much does a service cost?", "Service costs vary based on the type and complexity. You'll see pricing before booking.")
                HelpItem("What is your cancellation policy?", "You can cancel bookings up to 24 hours before the scheduled time without penalty.")
            }

            HelpSection("Support") {
                HelpItem("How do I contact support?", "You can reach us through the app's chat feature or email us at support@electrofix.com")
                HelpItem("What are your business hours?", "Our support team is available Monday-Friday, 9 AM - 6 PM EST.")
            }
        }
    }
}

@Composable
fun HelpSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
fun HelpItem(question: String, answer: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
