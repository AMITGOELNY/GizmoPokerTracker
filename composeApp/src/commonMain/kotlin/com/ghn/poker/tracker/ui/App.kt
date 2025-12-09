package com.ghn.poker.tracker.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ghn.poker.core.common.navigation.TabDestination
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.feature.auth.domain.usecase.impl.AppState
import com.ghn.poker.feature.auth.domain.usecase.impl.MainAction
import com.ghn.poker.feature.auth.domain.usecase.impl.MainViewModel
import com.ghn.poker.feature.auth.navigation.CreateAccount
import com.ghn.poker.feature.auth.navigation.Login
import com.ghn.poker.feature.auth.navigation.SplashScreen
import com.ghn.poker.feature.auth.navigation.Welcome
import com.ghn.poker.feature.auth.navigation.authEntryBuilder
import com.ghn.poker.feature.cards.navigation.cardsEntryBuilder
import com.ghn.poker.feature.feed.navigation.WebView
import com.ghn.poker.feature.feed.navigation.feedEntryBuilder
import com.ghn.poker.feature.tracker.navigation.SessionInsert
import com.ghn.poker.feature.tracker.navigation.TrackerHome
import com.ghn.poker.feature.tracker.navigation.trackerEntryBuilder
import com.ghn.poker.tracker.navigation.AppNavigationState
import com.ghn.poker.tracker.navigation.rememberAppNavigationState
import com.ghn.poker.tracker.ui.shared.bottomNavTabs
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun App(
    viewModel: MainViewModel = koinInject()
) {
    val navState = rememberAppNavigationState()

    GizmoTheme {
        val showBottomBar by remember {
            derivedStateOf { navState.shouldShowBottomBar() }
        }
        val isFullScreen by remember {
            derivedStateOf {
                val current = navState.currentDestination
                current == SplashScreen || current == Welcome
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                BottomNavigationBar(navState, showBottomBar)
            },
        ) { padding ->
            AppStateListener(viewModel, navState)
            NavDisplay(
                backStack = navState.backStack,
                modifier = if (isFullScreen) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.padding(bottom = padding.calculateBottomPadding())
                },
                entryProvider = entryProvider {
                    // Auth feature navigation
                    authEntryBuilder(
                        onSplashScreenFinished = { viewModel.onDispatch(MainAction.CheckForToken) },
                        onNavigateToLogin = { navState.navigate(Login) },
                        onNavigateToCreateAccount = { navState.navigate(CreateAccount) },
                        onBackClick = { navState.popBackStack() },
                        onSignOutClick = { viewModel.onDispatch(MainAction.SignOut) }
                    )

                    // Tracker feature navigation (Home tab)
                    trackerEntryBuilder(
                        onCreateSessionClick = { navState.navigate(SessionInsert) },
                        onBackClick = { navState.popBackStack() }
                    )

                    // Feed feature navigation (News tab)
                    feedEntryBuilder(
                        onNavigateToWebView = { url -> navState.navigate(WebView(url)) },
                        onBackClick = { navState.popBackStack() }
                    )

                    // Cards feature navigation (Cards tab)
                    cardsEntryBuilder()
                }
            )
        }
    }
}

@Composable
private fun AppStateListener(
    viewModel: MainViewModel,
    navState: AppNavigationState
) {
    LaunchedEffect(Unit) {
        viewModel.state.collect { state ->
            when (state.appState) {
                AppState.Init -> Unit
                AppState.LoggedIn -> navState.navigateAndClearBackStack(TrackerHome)
                AppState.LoggedOut -> navState.navigateAndClearBackStack(Welcome)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navState: AppNavigationState, visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar {
                val currentDestination = navState.currentDestination

                bottomNavTabs.forEach { tab ->
                    val selected by remember(currentDestination) {
                        derivedStateOf {
                            currentDestination is TabDestination &&
                                (currentDestination as TabDestination).tabIndex == tab.destination.tabIndex
                        }
                    }
                    NavigationBarItem(
                        icon = { Icon(painterResource(tab.icon), contentDescription = tab.title) },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        alwaysShowLabel = true,
                        selected = selected,
                        onClick = {
                            navState.navigateToTab(tab.destination)
                        },
                    )
                }
            }
        }
    )
}
