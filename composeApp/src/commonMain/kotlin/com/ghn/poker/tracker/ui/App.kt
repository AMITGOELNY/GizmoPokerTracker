package com.ghn.poker.tracker.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.tracker.SessionEntryScreen
import com.ghn.poker.tracker.ui.tracker.TrackerLandingPage
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

enum class Screen {
    SESSION,
    SESSION_INSERT;

    val formatted: String get() = "/${name.lowercase()}"
}

@Composable
fun App() {
    GizmoTheme {
        PreComposeApp {
            val navigator = rememberNavigator()
            Scaffold {
                NavHost(
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = Screen.SESSION.formatted,
                ) {
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
