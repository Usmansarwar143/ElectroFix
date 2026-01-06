package com.example.electrofix.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.electrofix.ui.components.AuthTextField
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.viewmodels.AIDiagnosticViewModel
import com.example.electrofix.viewmodels.DiagnosticResult
import com.example.electrofix.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIDiagnosticScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    diagnosticViewModel: AIDiagnosticViewModel = hiltViewModel()
) {
    val userState by userViewModel.uiState.collectAsState()
    val diagnosticState by diagnosticViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Premium Gate
    if (!userState.isPremium) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.GO_PREMIUM)
        }
        return // Prevent further composition
    }

    // Error Snackbar
    LaunchedEffect(diagnosticState.error) {
        diagnosticState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        diagnosticViewModel.onImageSelected(uri)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("AI Problem Diagnostic") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Image Picker ---
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (diagnosticState.selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(diagnosticState.selectedImageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add Photo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Problem Description ---
            AuthTextField(
                value = diagnosticState.problemDescription,
                onValueChange = diagnosticViewModel::onProblemDescriptionChanged,
                label = "Describe the problem...",
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Action Button ---
            Button(
                onClick = { diagnosticViewModel.runDiagnostic() },
                enabled = diagnosticState.selectedImageUri != null && diagnosticState.problemDescription.isNotBlank() && !diagnosticState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (diagnosticState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Get Diagnosis", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Result Display ---
            diagnosticState.result?.let {
                ResultCard(it)
            }
        }
    }
}

@Composable
private fun ResultCard(result: DiagnosticResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("AI Diagnosis Result", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            ResultRow("Problem", result.problem)
            ResultRow("Recommendation", result.recommendation)
            ResultRow("Confidence", result.confidence)
            Spacer(modifier = Modifier.height(12.dp))
            Text(result.disclaimer, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}
