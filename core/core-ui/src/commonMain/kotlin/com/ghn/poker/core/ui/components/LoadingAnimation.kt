package com.ghn.poker.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingAnimation(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    circleSize: Dp = 36.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {
    val circles = remember(initialAlpha) {
        listOf(
            Animatable(initialValue = initialAlpha),
            Animatable(initialValue = initialAlpha),
            Animatable(initialValue = initialAlpha)
        )
    }

    LaunchedEffect(circles) {
        circles.forEachIndexed { index, animatable ->
            launch {
                // Use coroutine delay to sync animations
                delay(timeMillis = (animationDelay / circles.size).toLong() * index)

                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = animationDelay),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
    }

    // container for circles
    Row {
        circles.forEachIndexed { index, animatable ->
            if (index != 0) Spacer(modifier = Modifier.width(width = 6.dp))

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(color = circleColor.copy(alpha = animatable.value))
            ) {}
        }
    }
}
