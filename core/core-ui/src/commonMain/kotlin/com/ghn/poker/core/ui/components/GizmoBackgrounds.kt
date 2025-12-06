package com.ghn.poker.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.GizmoGradients
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Slate
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Animated ambient background with subtle orbs
 */
@Composable
fun GizmoAmbientBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = ChampagneGold,
    secondaryColor: Color = Slate,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GizmoGradients.backgroundSurface)
    ) {
        // Top-left orb
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp + offset1.dp, y = (-50).dp)
                .alpha(0.12f)
                .blur(80.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor, Color.Transparent)
                        )
                    )
                }
        )

        // Bottom-right orb
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp + offset2.dp, y = 100.dp)
                .alpha(0.08f)
                .blur(60.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(secondaryColor, Color.Transparent)
                        )
                    )
                }
        )
    }
}

@Preview
@Composable
private fun GizmoAmbientBackgroundPreview() {
    GizmoTheme {
        GizmoAmbientBackground(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun GizmoAmbientBackgroundCustomColorsPreview() {
    GizmoTheme {
        GizmoAmbientBackground(
            modifier = Modifier.fillMaxSize(),
            primaryColor = Ruby,
            secondaryColor = ChampagneGold
        )
    }
}
