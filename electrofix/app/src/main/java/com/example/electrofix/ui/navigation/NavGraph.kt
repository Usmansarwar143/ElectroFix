package com.example.electrofix.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.electrofix.ui.screens.*

@Composable
fun ElectroFixNavHost() {
    val navController = rememberNavController()

    // Removed the Scaffold with bottomBar logic from here
    // The MainScaffoldScreen now handles its own bottom navigation

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = Modifier,
        enterTransition = { androidx.compose.animation.slideInHorizontally(initialOffsetX = { 1000 }) },
        exitTransition = { androidx.compose.animation.slideOutHorizontally(targetOffsetX = { -1000 }) },
        popEnterTransition = { androidx.compose.animation.slideInHorizontally(initialOffsetX = { -1000 }) },
        popExitTransition = { androidx.compose.animation.slideOutHorizontally(targetOffsetX = { 1000 }) }
    ) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.ROLE) { RoleSelectionScreen(navController) }
        composable("${Routes.AUTH}?role={role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            AuthScreen(navController, initialRole = role)
        }
        composable(Routes.DASHBOARD_HOST) { MainScaffoldScreen(navController) }

        // The following screens are mostly navigated to from MainScaffoldScreen's inner NavHost,
        // but also need to be defined here for direct navigation from other top-level flows.
        // For example, if AIDiagnosticScreen needs to navigate to NotificationsScreen directly.
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.BOOKINGS) { BookingsScreen(navController) }
        composable(Routes.MESSAGES) { MessagesScreen(navController) }
        composable(Routes.AI_DIAGNOSTIC) { AIDiagnosticScreen(navController) }
        composable(Routes.AI_CHATBOT) { AIChatbotScreen(navController) }
        composable(Routes.NOTIFICATIONS) { NotificationScreen(navController) }
        composable(Routes.ALERTS) { AlertsScreen(navController) } // New Alerts Screen
        composable(Routes.PROFILE) { ProfileScreen(navController) } // New Profile Overview Screen
        composable(Routes.EDIT_PROFILE) { EditProfileScreen(navController) } // New Edit Profile Form
        composable(Routes.PAYMENT_METHODS) { PaymentMethodsScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(Routes.ABOUT) { AboutScreen(navController) }
        composable(Routes.FAQ) { FAQScreen(navController) }
        composable(Routes.CHANGE_PASSWORD) { ChangePasswordScreen(navController) }
        composable(Routes.GO_PREMIUM) { GoPremiumScreen(navController) }
        composable(Routes.PRIVACY) { PrivacyScreen(navController) }
        composable(Routes.FEEDBACK) { FeedbackScreen(navController) }
        composable(Routes.CREATE_PROFILE) { CreateProfileScreen(navController) }
        
        composable("${Routes.CHAT}/{conversationId}/{name}") { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: "Chat"
            ChatScreen(navController, conversationId, name)
        }
    }
}
