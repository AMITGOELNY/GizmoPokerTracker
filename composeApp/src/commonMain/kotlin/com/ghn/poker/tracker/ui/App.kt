package com.ghn.poker.tracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.theme.title200
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    GizmoTheme {
        PreComposeApp {
            val navigator = rememberNavigator()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(
                                text = "GiZMO POKER",
                                style = MaterialTheme.typography.title200.copy(
                                    color = Color(0xFFAFA21D)
                                )
                            )
                        },
                    )
                }
            ) { padding ->
                NavHost(
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
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
