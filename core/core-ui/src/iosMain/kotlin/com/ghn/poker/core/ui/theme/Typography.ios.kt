package com.ghn.poker.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@ReadOnlyComposable
@Composable
internal actual fun getScreenWidthDp(): Int {
    return LocalWindowInfo.current.containerSize.width
}
