package com.ghn.poker.core.common.navigation

// import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavKey

/**
 * Base interface for all navigation keys in the app.
 * All route objects should implement this interface.
 */
interface GizmoNavKey : NavKey

/**
 * Marker interface for destinations that should show the bottom navigation bar.
 * Typically used for main tab destinations (Home, News, Cards, Settings).
 */
interface TabDestination : GizmoNavKey {
    val tabIndex: Int
}
