package com.ghn.poker.core.ui.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ghn.poker.core.ui.theme.GizmoTheme

@Composable
fun SurfacePreview(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    GizmoTheme { Surface(modifier) { content() } }
