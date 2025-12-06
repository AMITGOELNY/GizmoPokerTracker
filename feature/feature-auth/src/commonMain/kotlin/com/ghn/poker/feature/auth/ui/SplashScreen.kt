package com.ghn.poker.feature.auth.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gizmopoker.core.core_resources.generated.resources.Res
import gizmopoker.core.core_resources.generated.resources.app_name
import gizmopoker.core.core_resources.generated.resources.ic_clubs
import gizmopoker.core.core_resources.generated.resources.ic_diamond
import gizmopoker.core.core_resources.generated.resources.ic_hearts
import gizmopoker.core.core_resources.generated.resources.ic_spades
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.sin
import kotlin.random.Random

// Color palette
private val Gold = Color(0xFFFFD700)
private val DarkGold = Color(0xFFC5A572)
private val Black = Color(0xFF000000)
private val Charcoal = Color(0xFF1a1a1a)
private val White = Color(0xFFFFFFFF)

@Composable
fun SplashScreen(onSplashScreenFinished: () -> Unit) {
    var animationStarted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationStarted = true
        delay(2000) // Total duration
        onSplashScreenFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(0, 0, 0, 0)),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: Animated gradient background
        AnimatedGradientBackground(animationStarted)

        // Layer 2: Particle system
        GoldenParticles(animationStarted)

        // Layer 3: Card dealing animation
        DealingCards(animationStarted)

        // Layer 4: Logo text reveal
        LogoTextReveal(animationStarted)
    }
}

@Composable
private fun AnimatedGradientBackground(started: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient offset"
    )

    val alpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(300),
        label = "background alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Charcoal,
                        Black,
                        Black
                    ),
                    center = Offset(
                        x = 0.5f + gradientOffset * 0.1f,
                        y = 0.5f + gradientOffset * 0.1f
                    )
                )
            )
    )
}

data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val alpha: Float,
    val speedX: Float,
    val speedY: Float,
    val phase: Float
)

@Composable
private fun GoldenParticles(started: Boolean) {
    val particles = remember {
        List(40) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 3f + 1f,
                alpha = Random.nextFloat() * 0.6f + 0.3f,
                speedX = (Random.nextFloat() - 0.5f) * 0.02f,
                speedY = (Random.nextFloat() - 0.5f) * 0.02f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle time"
    )

    val particleAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "particle alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val offsetX = sin((time + particle.phase) * 0.017f) * 20f
            val offsetY = sin((time + particle.phase + 90f) * 0.013f) * 15f

            drawCircle(
                color = Gold,
                radius = particle.size,
                center = Offset(
                    x = size.width * particle.x + offsetX,
                    y = size.height * particle.y + offsetY
                ),
                alpha = particle.alpha * particleAlpha
            )
        }
    }
}

private data class SplashCardData(
    val icon: org.jetbrains.compose.resources.DrawableResource,
    val delay: Int
)

@Composable
private fun DealingCards(started: Boolean) {
    val cards = listOf(
        SplashCardData(Res.drawable.ic_spades, 0),
        SplashCardData(Res.drawable.ic_hearts, 150),
        SplashCardData(Res.drawable.ic_clubs, 300),
        SplashCardData(Res.drawable.ic_diamond, 450)
    )

    val positions = listOf(
        Offset(-0.3f, -0.3f),
        Offset(0.3f, -0.3f),
        Offset(-0.3f, 0.3f),
        Offset(0.3f, 0.3f)
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        cards.forEachIndexed { index, cardData ->
            AnimatedCard(
                icon = cardData.icon,
                started = started,
                delayMillis = cardData.delay + 300,
                targetPosition = positions[index],
                rotation = (index - 1.5f) * 15f
            )
        }
    }
}

@Composable
private fun AnimatedCard(
    icon: org.jetbrains.compose.resources.DrawableResource,
    started: Boolean,
    delayMillis: Int,
    targetPosition: Offset,
    rotation: Float
) {
    val scale by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "card scale"
    )

    val offsetX by animateFloatAsState(
        targetValue = if (started) targetPosition.x else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "card offsetX"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (started) targetPosition.y else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "card offsetY"
    )

    val cardRotation by animateFloatAsState(
        targetValue = if (started) rotation else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "card rotation"
    )

    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = Modifier
            .size(60.dp)
            .offset(x = (offsetX * 100).dp, y = (offsetY * 100).dp)
            .scale(scale * 0.6f)
            .rotate(cardRotation)
            .alpha(scale * 0.3f),
        tint = DarkGold
    )
}

@Composable
private fun LogoTextReveal(started: Boolean) {
    val text = stringResource(Res.string.app_name)

    // Overall text scale with pulse
    val textScale by animateFloatAsState(
        targetValue = if (started) 1f else 0.75f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "text scale"
    )

    // Pulse effect at the end
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(1500)
        ),
        label = "pulse"
    )

    // Shimmer effect
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(800)
        ),
        label = "shimmer"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 600
        ),
        label = "text alpha"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
            color = White,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = textScale * if (started) pulse else 1f
                    scaleY = textScale * if (started) pulse else 1f
                    alpha = textAlpha
                }
        )

        // Gold shimmer overlay
        if (started) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = Gold,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = textScale * pulse
                        scaleY = textScale * pulse
                        alpha = ((shimmerOffset + 1f) / 2f).coerceIn(0f, 0.6f)
                    }
            )
        }
    }
}
