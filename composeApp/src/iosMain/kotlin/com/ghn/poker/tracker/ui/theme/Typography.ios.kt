package com.ghn.poker.tracker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun getScreenWidthDp(): Int {
    return LocalWindowInfo.current.containerSize.width
}
