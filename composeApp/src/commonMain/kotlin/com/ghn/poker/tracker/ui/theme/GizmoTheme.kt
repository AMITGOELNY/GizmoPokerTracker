package com.ghn.poker.tracker.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun GizmoTheme(content: @Composable () -> Unit) {
//    val isSmallScreen = getScreenWidthDp() <= 360
//    val typography = if (isSmallScreen) WildHealthSmallTypography else WildHealthTypography
//    val dimensions = if (isSmallScreen) smallDimensions else sw360Dimensions

    ProvideDimens(dimensions = sw360Dimensions) {
        MaterialTheme(
            colors = colors,
            shapes = Shapes,
//            typography = typography,
            content = content
        )
    }
}

private object AppTheme {
    // val colors: Colors
    //     @Composable
    //     get() = LocalAppColors.current

    val dimens: Dimensions
        @Composable get() = LocalAppDimens.current
}

val Dimens: Dimensions
    @Composable get() = AppTheme.dimens
