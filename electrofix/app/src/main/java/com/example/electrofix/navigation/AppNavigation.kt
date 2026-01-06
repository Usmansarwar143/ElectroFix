package com.example.electrofix.navigation

/*
// This AppNavigation composable is deprecated and replaced by ElectroFixNavHost in NavGraph.kt
// and MainScaffoldScreen.kt. Commenting it out to resolve compilation errors.
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sosViewModel: SOSViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sosViewModel.sosTriggered.collect {
            scope.launch {
                snackbarHostState.showSnackbar("SOS Alert Sent to Emergency Contacts")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (currentRoute in bottomNavScreens.map { it.route }) {
                FloatingActionButton(
                    onClick = { sosViewModel.onSosButtonPressed() },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "SOS", tint = MaterialTheme.colorScheme.onSecondary)
                }
            }
        },
        bottomBar = {
            if (currentRoute in bottomNavScreens.map { it.route }) {
                NavigationBar {
                    bottomNavScreens.forEach { screen ->
                        AnimatedBottomNavigationItem(
                            screen = screen,
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) { SplashScreen(navController) }
            composable(Screen.Auth.route) { AuthScreen(navController) }
            composable(Screen.RoleSelection.route) { RoleSelectionScreen(navController) }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Bookings.route) { BookingsScreen(navController) }
            composable(Screen.Messages.route) { MessagesScreen(navController) }
            composable(Screen.Chatbot.route) { ChatbotScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.BookingDetails.route) { BookingDetailsScreen(navController) }
            composable(Screen.EditProfile.route) { EditProfileScreen(navController) }
            composable(Screen.PaymentMethods.route) { PaymentMethodsScreen(navController) }
            composable(Screen.Address.route) { AddressScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.Help.route) { HelpScreen(navController) }
            composable(Screen.Chat.route) { ChatScreen(navController) }
        }
    }
}

@Composable
fun RowScope.AnimatedBottomNavigationItem(
    screen: Screen,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (screen) {
        Screen.Home -> Icons.Outlined.Home
        Screen.Bookings -> Icons.Outlined.DateRange
        Screen.Messages -> Icons.Outlined.ChatBubbleOutline
        Screen.Chatbot -> Icons.Outlined.SupportAgent
        Screen.Profile -> Icons.Outlined.AccountCircle
        else -> Icons.Default.Warning // Should not happen
    }

    val scale by animateFloatAsState(targetValue = if (selected) 1.2f else 1.0f)
    val rotation by animateFloatAsState(targetValue = if (selected) 360f else 0f)

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "${screen.route} Icon",
                modifier = Modifier
                    .scale(scale)
                    .rotate(rotation),
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
*/
