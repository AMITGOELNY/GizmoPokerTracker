package com.ghn.poker.feature.tracker.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ghn.poker.feature.tracker.ui.SessionEntryScreen
import com.ghn.poker.feature.tracker.ui.TrackerLandingPage

fun NavGraphBuilder.trackerNavGraph(
    onCreateSessionClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onBackClick: () -> Unit,
    onShowBottomBar: (Boolean) -> Unit
) {
    composable<TrackerHome>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        onShowBottomBar(true)
        TrackerLandingPage(
            onCreateSessionClick = {
                onShowBottomBar(false)
                onCreateSessionClick()
            },
            onSignOutClick = onSignOutClick
        )
    }

    composable<SessionInsert> {
        SessionEntryScreen(onBackClick = onBackClick)
    }
}
