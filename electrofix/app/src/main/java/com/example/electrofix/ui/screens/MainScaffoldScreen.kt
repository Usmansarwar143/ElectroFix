package com.example.electrofix.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.viewmodels.MainUiState
import com.example.electrofix.viewmodels.MainViewModel

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

@Composable
fun MainScaffoldScreen(
    appNavController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is MainUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MainUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // In a real app, show an error screen or redirect to Auth
                // For now, redirecting to Auth
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    appNavController.navigate(Routes.AUTH) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
        }
        is MainUiState.Success -> {
            val user = state.user
            val isTechnician = user.role.equals("Technician", ignoreCase = true)

            val bottomNavItems = if (isTechnician) {
                listOf(
                    BottomNavItem("Jobs", Icons.Outlined.WorkOutline, Routes.TECHNICIAN_HOME),
                    BottomNavItem("Earnings", Icons.Outlined.MonetizationOn, Routes.EARNINGS),
                    BottomNavItem("Messages", Icons.Outlined.ChatBubbleOutline, Routes.MESSAGES),
                    BottomNavItem("Profile", Icons.Outlined.AccountCircle, Routes.PROFILE)
                )
            } else {
                listOf(
                    BottomNavItem("Home", Icons.Outlined.Home, Routes.HOME),
                    BottomNavItem("Bookings", Icons.Outlined.DateRange, Routes.BOOKINGS),
                    BottomNavItem("Messages", Icons.Outlined.ChatBubbleOutline, Routes.MESSAGES),
                    BottomNavItem("Chatbot", Icons.Outlined.SupportAgent, Routes.AI_CHATBOT),
                    BottomNavItem("Profile", Icons.Outlined.AccountCircle, Routes.PROFILE)
                )
            }

            val startDestination = if (isTechnician) Routes.TECHNICIAN_HOME else Routes.HOME

            Scaffold(
                bottomBar = {
                    GlassmorphicBottomNavigation(
                        items = bottomNavItems,
                        currentRoute = currentRoute,
                        onItemClick = { item ->
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = bottomNavController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // Customer Routes
                    composable(Routes.HOME) { HomeScreen(appNavController) }
                    composable(Routes.BOOKINGS) { BookingsScreen(appNavController) }
                    composable(Routes.AI_CHATBOT) { AIChatbotScreen(appNavController) }
                    
                    // Technician Routes
                    composable(Routes.TECHNICIAN_HOME) { TechnicianHomeScreen(appNavController) }
                    composable(Routes.EARNINGS) { EarningsScreen(appNavController) }

                    // Shared Routes
                    composable(Routes.MESSAGES) { MessagesScreen(appNavController) }
                    composable(Routes.PROFILE) { ProfileScreen(appNavController) }
                }
            }
        }
    }
}

@Composable
fun GlassmorphicBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    // Glassmorphic effect using a semi-transparent surface with blur (if supported) or gradient
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.height(80.dp)
            ) {
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onItemClick(item) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        alwaysShowLabel = false
                    )
                }
            }
        }
    }
}
