package com.ghn.poker.tracker.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ghn.poker.tracker.domain.usecase.impl.UserStore
import com.ghn.poker.tracker.ui.login.LoginScreen
import com.ghn.poker.tracker.ui.login.SplashScreen
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

enum class Screen {
    LOGIN,
    SESSION,
    SESSION_INSERT,
    SPLASH_SCREEN;

    val formatted: String get() = "/${name.lowercase()}"
}

@Composable
fun App(viewModel: UserStore = UserStore()) {
    GizmoTheme {
        Scaffold {
            PreComposeApp {
                val navigator = rememberNavigator()

                NavHost(
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = Screen.LOGIN.formatted,
                ) {
                    scene(
                        route = Screen.SPLASH_SCREEN.formatted,
                        navTransition = NavTransition()
                    ) {
                        SplashScreen(onSplashScreenFinished = viewModel::checkForToken)
                    }

                    scene(
                        route = Screen.LOGIN.formatted,
                        navTransition = NavTransition()
                    ) {
                        LoginScreen {
                            navigator.navigate(Screen.SESSION.formatted)
                        }
                    }
                    scene(route = Screen.SESSION.formatted, navTransition = NavTransition()) {
                        TrackerLandingPage {
                            navigator.navigate(Screen.SESSION_INSERT.formatted)
                        }
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
