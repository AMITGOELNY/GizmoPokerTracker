package com.ghn.poker.tracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ghn.poker.feature.auth.domain.usecase.impl.AppState
import com.ghn.poker.feature.auth.domain.usecase.impl.Store
import com.ghn.poker.feature.auth.navigation.CreateAccount
import com.ghn.poker.feature.auth.navigation.Login
import com.ghn.poker.feature.auth.navigation.SplashScreen
import com.ghn.poker.feature.auth.navigation.Welcome
import com.ghn.poker.feature.auth.navigation.authNavGraph
import com.ghn.poker.feature.cards.navigation.CardsHome
import com.ghn.poker.feature.cards.navigation.cardsNavGraph
import com.ghn.poker.feature.feed.navigation.FeedHome
import com.ghn.poker.feature.feed.navigation.WebView
import com.ghn.poker.feature.feed.navigation.feedNavGraph
import com.ghn.poker.feature.tracker.navigation.SessionInsert
import com.ghn.poker.feature.tracker.navigation.TrackerHome
import com.ghn.poker.feature.tracker.navigation.trackerNavGraph
import com.ghn.poker.tracker.ui.shared.BottomNavItem
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun App(
    viewModel: Store<AppState> = koinInject(),
    navController: NavHostController = rememberNavController()
) {
    GizmoTheme {
        val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        val isFullScreen by remember {
            derivedStateOf {
                currentRoute == SplashScreen::class.qualifiedName ||
                    currentRoute == Welcome::class.qualifiedName
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                BottomNavigationBar(navController, bottomBarState)
            },
        ) { padding ->
            AppStateListener(viewModel, navController)
            NavHost(
                navController = navController,
                startDestination = SplashScreen,
                modifier = if (isFullScreen) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.padding(bottom = padding.calculateBottomPadding())
                }
            ) {
                // Auth feature navigation
                authNavGraph(
                    onSplashScreenFinished = viewModel::checkForToken,
                    onNavigateToLogin = { navController.navigate(Login) },
                    onNavigateToCreateAccount = { navController.navigate(CreateAccount) },
                    onBackClick = navController::popBackStack
                )

                // Tracker feature navigation (Home tab)
                trackerNavGraph(
                    onCreateSessionClick = { navController.navigate(SessionInsert) },
                    onSignOutClick = { viewModel.signout() },
                    onBackClick = navController::popBackStack,
                    onShowBottomBar = { bottomBarState.value = it }
                )

                // Feed feature navigation (News tab)
                feedNavGraph(
                    onNavigateToWebView = { url -> navController.navigate(WebView(url)) },
                    onBackClick = navController::popBackStack,
                    onShowBottomBar = { bottomBarState.value = it }
                )

                // Cards feature navigation (Profile tab)
                cardsNavGraph()

                // Bottom nav item routes that map to feature routes
                composable<BottomNavItem.Home>(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) {
                    bottomBarState.value = true
                    // Navigate to tracker home internally
                    LaunchedEffect(Unit) {
                        navController.navigate(TrackerHome) {
                            popUpTo(BottomNavItem.Home) { inclusive = true }
                        }
                    }
                }

                composable<BottomNavItem.News>(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) {
                    bottomBarState.value = true
                    LaunchedEffect(Unit) {
                        navController.navigate(FeedHome) {
                            popUpTo(BottomNavItem.News) { inclusive = true }
                        }
                    }
                }

                composable<BottomNavItem.Profile> {
                    bottomBarState.value = true
                    LaunchedEffect(Unit) {
                        navController.navigate(CardsHome) {
                            popUpTo(BottomNavItem.Profile) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppStateListener(
    viewModel: Store<AppState>,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.userState.collect { state ->
            when (state) {
                AppState.Init -> Unit
                AppState.LoggedIn -> navController.navigate(
                    route = TrackerHome,
                )

                AppState.LoggedOut -> navController.navigate(
                    route = Welcome,
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val items = remember {
        listOf(BottomNavItem.Home, BottomNavItem.News, BottomNavItem.Profile)
    }
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                    ?: TrackerHome::class.qualifiedName.orEmpty()

                items.forEachIndexed { index, item ->
                    val featureRoute = when (index) {
                        0 -> TrackerHome::class.qualifiedName
                        1 -> FeedHome::class.qualifiedName
                        2 -> CardsHome::class.qualifiedName
                        else -> null
                    }
                    val selected by remember(currentRoute) {
                        derivedStateOf { currentRoute == featureRoute }
                    }
                    NavigationBarItem(
                        icon = { Icon(painterResource(item.icon()), contentDescription = null) },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        alwaysShowLabel = true,
                        selected = selected,
                        onClick = {
                            val destination = when (index) {
                                0 -> TrackerHome
                                1 -> FeedHome
                                2 -> CardsHome
                                else -> return@NavigationBarItem
                            }
                            navController.navigate(destination) {
                                launchSingleTop = true
                                popUpTo(TrackerHome) {
                                    inclusive = false
                                }
                            }
                        },
                    )
                }
            }
        }
    )
}
