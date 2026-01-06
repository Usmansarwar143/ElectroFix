package com.example.electrofix.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.electrofix.R
import com.example.electrofix.ui.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun RoleSelectionScreen(navController: NavController) {

    val logoAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(50f) }
    val button1Alpha = remember { Animatable(0f) }
    val button1OffsetY = remember { Animatable(50f) }
    val button2Alpha = remember { Animatable(0f) }
    val button2OffsetY = remember { Animatable(50f) }

    LaunchedEffect(key1 = true) {
        logoAlpha.animateTo(1f, tween(800))
        delay(200)
        titleAlpha.animateTo(1f, tween(800))
        titleOffsetY.animateTo(0f, tween(800))
        delay(200)
        button1Alpha.animateTo(1f, tween(800))
        button1OffsetY.animateTo(0f, tween(800))
        delay(100)
        button2Alpha.animateTo(1f, tween(800))
        button2OffsetY.animateTo(0f, tween(800))
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "ElectroFix Logo",
                modifier = Modifier
                    .size(200.dp) // Increased logo size
                    .alpha(logoAlpha.value)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "How will you be using ElectroFix?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = { navController.navigate("${Routes.AUTH}?role=Customer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(button1Alpha.value)
                    .offset(y = button1OffsetY.value.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("I need a repair (Customer)")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("${Routes.AUTH}?role=Technician") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(button2Alpha.value)
                    .offset(y = button2OffsetY.value.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("I am a Technician")
            }
        }
    }
}