package com.ghn.poker.tracker.navigation

import com.ghn.poker.core.common.navigation.GizmoNavKey

interface NavigationActions {
    fun navigate(destination: GizmoNavKey)
    fun navigateAndClearBackStack(destination: GizmoNavKey)
    fun popBackStack(): Boolean
}
