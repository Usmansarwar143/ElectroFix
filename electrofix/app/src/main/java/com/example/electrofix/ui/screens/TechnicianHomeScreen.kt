package com.example.electrofix.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.viewmodels.ProfileViewModel

@Composable
fun TechnicianHomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    val userName = profileUiState.userProfile?.name ?: "Technician"

    // Dummy Data
    val availableJobs = listOf(
        Job("1", "AC Repair", "123 Main St, New York", "$120", "Open"),
        Job("2", "Washing Machine Fix", "456 Elm Ave, Brooklyn", "$85", "Open"),
        Job("3", "Electrical Rewiring", "789 Pine Ln, Queens", "$200", "Pending"),
        Job("4", "Refrigerator Service", "321 Oak Blvd, Bronx", "$95", "Open"),
        Job("5", "Microwave Repair", "654 Maple Dr, Staten Island", "$60", "Open")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Hello,", style = MaterialTheme.typography.headlineSmall)
                Text(userName, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            }
            Row {
                IconButton(onClick = { navController.navigate(Routes.NOTIFICATIONS) }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { navController.navigate(Routes.ALERTS) }) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "Alerts",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Technician Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Available Jobs",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for Bottom nav
        ) {
            items(availableJobs) { job ->
                JobCard(job)
            }
        }
    }
}
// JobCard and Job data class remain the same, kept in file but omitted for brevity if unchanged logic needed
// Included for completeness in file write
@Composable
fun JobCard(job: Job) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Navigate to job details */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = job.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = job.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ${job.status}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (job.status == "Open") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
                Button(onClick = { /* TODO: Accept Job */ }) {
                    Text("Details")
                }
            }
        }
    }
}

data class Job(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val status: String
)
