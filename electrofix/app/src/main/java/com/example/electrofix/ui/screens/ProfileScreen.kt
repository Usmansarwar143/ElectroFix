
package com.example.electrofix.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.electrofix.ui.components.GlassmorphicCard
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.ui.theme.ElectroFixTheme
import com.example.electrofix.viewmodels.ProfileViewModel
import com.example.electrofix.viewmodels.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    ElectroFixTheme(darkTheme = isDarkTheme) {
        val uiState by profileViewModel.uiState.collectAsState()
        val userName = uiState.userProfile?.name ?: "EFUser"
        val userEmail = uiState.userProfile?.email ?: "user.email@example.com"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Profile Picture
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = if (userName.isNotEmpty()) userName.first().uppercase() else "U",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Name and Email
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Profile Options
                ProfileOption(icon = Icons.Default.Edit, text = "Edit Profile") { navController.navigate(Routes.EDIT_PROFILE) }
                ProfileOption(icon = Icons.Default.Lock, text = "Change Password") { navController.navigate(Routes.CHANGE_PASSWORD) }
                ProfileOption(icon = Icons.Default.PrivacyTip, text = "Privacy") { navController.navigate(Routes.PRIVACY) }
                ProfileOption(icon = Icons.Default.Settings, text = "Settings") { navController.navigate(Routes.SETTINGS) }
                ProfileOption(icon = Icons.Default.Stars, text = "Go Premium") { navController.navigate(Routes.GO_PREMIUM) }
                ProfileOption(icon = Icons.AutoMirrored.Filled.HelpOutline, text = "Help and Support") { navController.navigate(Routes.FAQ) }
                ProfileOption(icon = Icons.Default.Feedback, text = "Feedback") { navController.navigate(Routes.FEEDBACK) }
                DarkModeToggle(isDarkTheme = isDarkTheme, onThemeChange = { themeViewModel.setDarkTheme(it) })

                // Logout Option
                ProfileOption(icon = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ExitToApp, text = "Logout", isLogout = true) {
                    profileViewModel.logout()
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, text: String, isLogout: Boolean = false, onClick: () -> Unit) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = if (isLogout) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isLogout) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DarkModeToggle(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Dark Mode",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Switch(checked = isDarkTheme, onCheckedChange = onThemeChange)
        }
    }
}
