
package com.example.electrofix.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.electrofix.R
import com.example.electrofix.ui.components.AuthTextField
import com.example.electrofix.ui.components.GlassmorphicCard
import com.example.electrofix.ui.navigation.Routes
import com.example.electrofix.ui.theme.ElectroFixTheme
import com.example.electrofix.viewmodels.AuthScreenState
import com.example.electrofix.viewmodels.AuthUiState
import com.example.electrofix.viewmodels.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    initialRole: String? = null
) {
    val authScreenState by viewModel.authScreenState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()

    LaunchedEffect(initialRole) {
        if (initialRole != null) {
            viewModel.onRoleSelected(initialRole)
            if (initialRole == "Technician" || initialRole == "Customer") {
                viewModel.showSignUp()
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> {
                if (state.isNewUser) {
                    navController.navigate(Routes.CREATE_PROFILE) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.DASHBOARD_HOST) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
                viewModel.resetState()
            }

            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (uiState is AuthUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "ElectroFix Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 32.dp) // Increased size
                )
                GlassmorphicCard {
                    AnimatedContent(
                        targetState = authScreenState,
                        transitionSpec = {
                            if (targetState.ordinal > initialState.ordinal) {
                                (slideInHorizontally { width -> width } + fadeIn(animationSpec = tween(400))).togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut(animationSpec = tween(400)))
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn(animationSpec = tween(400))).togetherWith(
                                    slideOutHorizontally { width -> width } + fadeOut(animationSpec = tween(400)))
                            }.using(SizeTransform(clip = false))
                        }, label = "AuthAnimation"
                    ) { targetState ->
                        when (targetState) {
                            AuthScreenState.LOGIN -> LoginForm(
                                email = email,
                                password = password,
                                onEmailChange = viewModel::onEmailChange,
                                onPasswordChange = viewModel::onPasswordChange,
                                onLoginClick = viewModel::handleLogin,
                                onShowSignUp = viewModel::showSignUp,
                                onForgotPassword = viewModel::showForgotPassword
                            )

                            AuthScreenState.SIGN_UP -> SignUpForm(
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword,
                                selectedRole = selectedRole,
                                onEmailChange = viewModel::onEmailChange,
                                onPasswordChange = viewModel::onPasswordChange,
                                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                                onRoleSelected = viewModel::onRoleSelected,
                                onSignUpClick = viewModel::handleSignUp,
                                onShowLogin = viewModel::showLogin
                            )

                            AuthScreenState.FORGOT_PASSWORD -> ForgotPasswordForm(
                                email = email,
                                onEmailChange = viewModel::onEmailChange,
                                onResetClick = viewModel::handlePasswordReset,
                                onBackToLogin = viewModel::showLogin
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LoginForm(
    email: String, password: String, onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit, onLoginClick: () -> Unit,
    onShowSignUp: () -> Unit, onForgotPassword: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome Back!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            isPassword = true
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Login") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onForgotPassword) { Text("Forgot Password?") }
        TextButton(onClick = onShowSignUp) { Text("Don't have an account? Sign Up") }
    }
}

@Composable
fun SignUpForm(
    email: String, password: String, confirmPassword: String, selectedRole: String,
    onEmailChange: (String) -> Unit, onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit, onRoleSelected: (String) -> Unit,
    onSignUpClick: () -> Unit, onShowLogin: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            isPassword = true
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm Password",
            isPassword = true
        )
        Spacer(Modifier.height(16.dp))
        RoleSelector(selectedRole, onRoleSelected)
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Sign Up") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onShowLogin) { Text("Already have an account? Login") }
    }
}

@Composable
fun RoleSelector(selectedRole: String, onRoleSelected: (String) -> Unit) {
    val roles = listOf("Customer", "Technician")
    Column {
        Text(
            "I am a:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row {
            roles.forEach { role ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onRoleSelected(role) }
                ) {
                    RadioButton(selected = selectedRole == role, onClick = { onRoleSelected(role) })
                    Text(
                        text = role,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordForm(
    email: String, onEmailChange: (String) -> Unit, onResetClick: () -> Unit, onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Reset Password",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Enter the email associated with your account...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Send Instructions") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onBackToLogin) { Text("Back to Login") }
    }
}
