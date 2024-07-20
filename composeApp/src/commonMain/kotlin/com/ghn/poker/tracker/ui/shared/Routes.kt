package com.ghn.poker.tracker.ui.shared

import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.ic_baseline_assignment
import gizmopoker.composeapp.generated.resources.ic_home
import gizmopoker.composeapp.generated.resources.ic_person
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data object Welcome

@Serializable
data object Login

@Serializable
data object CreateAccount

@Serializable
data object SessionInsert

@Serializable
data object SplashScreen

@Serializable
data class WebView(val url: String)

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
    data object Profile : BottomNavItem("profile", "Account")

    fun icon(): DrawableResource {
        return when (this) {
            Home -> Res.drawable.ic_home
            News -> Res.drawable.ic_baseline_assignment
            Profile -> Res.drawable.ic_person
        }
    }
}
