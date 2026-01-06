package com.example.electrofix.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.electrofix.MainActivity
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_isDisplayed_byDefault() {
        composeTestRule.setContent {
            AuthScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
    }
}
