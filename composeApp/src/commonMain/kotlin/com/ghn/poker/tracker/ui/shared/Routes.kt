package com.ghn.poker.tracker.ui.shared

import gizmopoker.core.core_ui.generated.resources.Res
import gizmopoker.core.core_ui.generated.resources.ic_baseline_assignment
import gizmopoker.core.core_ui.generated.resources.ic_home
import gizmopoker.core.core_ui.generated.resources.ic_settings
import gizmopoker.core.core_ui.generated.resources.ic_spade
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
sealed class BottomNavItem(
    val route: String,
    val title: String
) {
    @Serializable
    data object Home : BottomNavItem("home", "Home")

    @Serializable
    data object News : BottomNavItem("news", "News")

    @Serializable
    data object Cards : BottomNavItem("cards", "Cards")

    @Serializable
    data object Settings : BottomNavItem("settings", "Settings")

    fun icon(): DrawableResource {
        return when (this) {
            Home -> Res.drawable.ic_home
            News -> Res.drawable.ic_baseline_assignment
            Cards -> Res.drawable.ic_spade
            Settings -> Res.drawable.ic_settings
        }
    }
}
