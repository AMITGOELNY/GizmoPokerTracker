package com.ghn.poker.tracker.ui.shared

import com.ghn.poker.core.common.navigation.TabDestination
import com.ghn.poker.feature.auth.navigation.SettingsHome
import com.ghn.poker.feature.cards.navigation.CardsHome
import com.ghn.poker.feature.feed.navigation.FeedHome
import com.ghn.poker.feature.tracker.navigation.TrackerHome
import gizmopoker.core.core_ui.generated.resources.Res
import gizmopoker.core.core_ui.generated.resources.ic_baseline_assignment
import gizmopoker.core.core_ui.generated.resources.ic_home
import gizmopoker.core.core_ui.generated.resources.ic_settings
import gizmopoker.core.core_ui.generated.resources.ic_spade
import org.jetbrains.compose.resources.DrawableResource

data class BottomNavTab(
    val destination: TabDestination,
    val title: String,
    val icon: DrawableResource
)

val bottomNavTabs = listOf(
    BottomNavTab(TrackerHome, "Home", Res.drawable.ic_home),
    BottomNavTab(FeedHome, "News", Res.drawable.ic_baseline_assignment),
    BottomNavTab(CardsHome, "Cards", Res.drawable.ic_spade),
    BottomNavTab(SettingsHome, "Settings", Res.drawable.ic_settings)
)
