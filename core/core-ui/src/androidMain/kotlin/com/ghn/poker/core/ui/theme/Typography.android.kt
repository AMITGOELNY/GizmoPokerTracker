package com.ghn.poker.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

@ReadOnlyComposable
@Composable
internal actual fun getScreenWidthDp(): Int {
    return LocalConfiguration.current.screenWidthDp
}
