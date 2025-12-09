package com.ghn.poker.feature.tracker.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.feature.tracker.ui.SessionEntryScreen
import com.ghn.poker.feature.tracker.ui.TrackerLandingPage

fun EntryProviderScope<GizmoNavKey>.trackerEntryBuilder(
    onCreateSessionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    entry<TrackerHome> {
        TrackerLandingPage(onCreateSessionClick = onCreateSessionClick)
    }

    entry<SessionInsert> {
        SessionEntryScreen(onBackClick = onBackClick)
    }
}
