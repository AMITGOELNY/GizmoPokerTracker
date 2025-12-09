package com.ghn.poker.feature.auth.navigation

import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.core.common.navigation.TabDestination
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreen : GizmoNavKey

@Serializable
data object Welcome : GizmoNavKey

@Serializable
data object Login : GizmoNavKey

@Serializable
data object CreateAccount : GizmoNavKey

@Serializable
data object SettingsHome : TabDestination {
    override val tabIndex: Int = 3
}
