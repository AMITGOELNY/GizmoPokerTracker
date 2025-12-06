package com.ghn.poker.tracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

// =============================================================================
// GIZMOPOKER THEME
// =============================================================================

@Composable
fun GizmoTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalGizmoColors provides GizmoColors(),
        LocalAppDimens provides sw360Dimensions
    ) {
        MaterialTheme(
            colorScheme = colors,
            shapes = Shapes,
            typography = Typography,
            content = content,
        )
    }
}

// -----------------------------------------------------------------------------
// THEME ACCESSORS
// -----------------------------------------------------------------------------

object GizmoTheme {
    val colors: GizmoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalGizmoColors.current

    val dimens: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimens.current
}

/** Quick access to dimensions */
val Dimens: Dimensions
    @Composable
    get() = GizmoTheme.dimens
