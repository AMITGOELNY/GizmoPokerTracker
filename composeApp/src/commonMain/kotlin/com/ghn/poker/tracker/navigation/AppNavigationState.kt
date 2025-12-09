package com.ghn.poker.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ghn.poker.core.common.navigation.GizmoNavKey
import com.ghn.poker.core.common.navigation.TabDestination
import com.ghn.poker.feature.auth.navigation.SplashScreen

@Stable
class AppNavigationState(
    val backStack: SnapshotStateList<GizmoNavKey>
) : NavigationActions {

    override fun navigate(destination: GizmoNavKey) {
        backStack.add(destination)
    }

    override fun navigateAndClearBackStack(destination: GizmoNavKey) {
        backStack.clear()
        backStack.add(destination)
    }

    override fun popBackStack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            true
        } else {
            false
        }
    }

    fun navigateToTab(destination: TabDestination) {
        // Find existing tab in back stack
        val existingIndex = backStack.indexOfFirst {
            it is TabDestination && it.tabIndex == destination.tabIndex
        }

        if (existingIndex >= 0) {
            // Pop to existing tab
            while (backStack.lastIndex > existingIndex) {
                backStack.removeAt(backStack.lastIndex)
            }
        } else {
            // Clear to first tab destination and add new tab
            val firstTabIndex = backStack.indexOfFirst { it is TabDestination }
            if (firstTabIndex >= 0) {
                while (backStack.lastIndex > firstTabIndex) {
                    backStack.removeAt(backStack.lastIndex)
                }
                backStack[firstTabIndex] = destination
            } else {
                backStack.add(destination)
            }
        }
    }

    fun shouldShowBottomBar(): Boolean {
        return backStack.lastOrNull() is TabDestination
    }

    val currentDestination: GizmoNavKey?
        get() = backStack.lastOrNull()
}

@Composable
fun rememberAppNavigationState(initialDestination: GizmoNavKey = SplashScreen): AppNavigationState {
    return remember {
        AppNavigationState(mutableStateListOf(initialDestination))
    }
}
