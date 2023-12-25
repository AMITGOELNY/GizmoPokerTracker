package com.ghn.poker.tracker.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ghn.poker.tracker.domain.usecase.impl.AppState
import com.ghn.poker.tracker.domain.usecase.impl.Store
import com.ghn.poker.tracker.ui.login.GetStartedScreen
import com.ghn.poker.tracker.ui.login.LoginScreen
import com.ghn.poker.tracker.ui.login.SplashScreen
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import kotlinx.coroutines.flow.collectLatest
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.koinInject

enum class Screen {
    WELCOME,
    LOGIN,
    SESSION,
    SESSION_INSERT,
    SPLASH_SCREEN;

    val formatted: String get() = "/${name.lowercase()}"
}

@Composable
fun App(viewModel: Store<AppState> = koinInject()) {
    GizmoTheme {
        Scaffold {
            PreComposeApp {
                val navigator = rememberNavigator()

                LaunchedEffect(Unit) {
                    viewModel.userState.collectLatest { state ->
                        when (state) {
                            AppState.Init -> Unit
                            AppState.LoggedIn -> navigator.navigate(Screen.SESSION.formatted)
                            AppState.LoggedOut -> navigator.navigate(Screen.WELCOME.formatted)
                        }
                    }
                }

                NavHost(
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = Screen.SPLASH_SCREEN.formatted,
                ) {
                    scene(
                        route = Screen.SPLASH_SCREEN.formatted,
                        navTransition = NavTransition()
                    ) {
                        SplashScreen(onSplashScreenFinished = viewModel::checkForToken)
                    }

                    scene(Screen.WELCOME.formatted, navTransition = NavTransition()) {
                        GetStartedScreen { navigator.navigate(Screen.LOGIN.formatted) }
                    }

                    scene(Screen.LOGIN.formatted, navTransition = NavTransition()) {
                        LoginScreen { navigator.goBack() }
                    }

                    scene(route = Screen.SESSION.formatted, navTransition = NavTransition()) {
                        TrackerLandingPage(
                            onCreateSessionClick = { navigator.navigate(Screen.SESSION_INSERT.formatted) },
                            onSignOutClick = { viewModel.signout() }
                        )
                    }

                    scene(
                        route = Screen.SESSION_INSERT.formatted,
                        navTransition = NavTransition()
                    ) {
                        SessionEntryScreen(onBackClick = navigator::goBack)
                    }
                }
            }
        }
    }
}
