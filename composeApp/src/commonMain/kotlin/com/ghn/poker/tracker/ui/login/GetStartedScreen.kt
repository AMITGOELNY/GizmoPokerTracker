package com.ghn.poker.tracker.ui.login

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.ui.preview.SurfacePreview
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.app_name
import gizmopoker.composeapp.generated.resources.create_account
import gizmopoker.composeapp.generated.resources.ic_clubs
import gizmopoker.composeapp.generated.resources.ic_diamond
import gizmopoker.composeapp.generated.resources.ic_hearts
import gizmopoker.composeapp.generated.resources.ic_spades
import gizmopoker.composeapp.generated.resources.select_option_to_start
import gizmopoker.composeapp.generated.resources.sign_in
import gizmopoker.composeapp.generated.resources.welcome_to
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.sin
import kotlin.random.Random

// Color palette - monochrome premium with mysterious touches
private val Black = Color(0xFF000000)
private val Navy = Color(0xFF0a0e1a)
private val DeepPurple = Color(0xFF1a0f2e)
private val Gold = Color(0xFFFFD700)
private val Champagne = Color(0xFFF5E6D3)
private val Platinum = Color(0xFFE5E4E2)
private val GhostWhite = Color(0xFFF8F8FF)

@Composable
fun GetStartedScreen(
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
) {
    var animationStarted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animationStarted = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1: Animated gradient background
        AnimatedMysteriousBackground(animationStarted)

        // Layer 2: Fog particles
        FogParticles(animationStarted)

        // Layer 3: Floating card silhouettes
        FloatingCardSilhouettes(animationStarted)

        // Layer 4: Hero imagery (center)
        PlayingCardsFan(animationStarted)

        // Layer 5: Ambient golden particles
        AmbientGoldenDust(animationStarted)

        // Layer 6: Content (text and buttons)
        WelcomeContent(
            animationStarted = animationStarted,
            onSignInClick = onSignInClick,
            onCreateAccountClick = onCreateAccountClick
        )
    }
}

// ============================================================================
// Background Layers
// ============================================================================

@Composable
private fun AnimatedMysteriousBackground(started: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(600),
        label = "background alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(backgroundAlpha)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Navy.copy(alpha = 0.3f),
                        Black,
                        DeepPurple.copy(alpha = 0.2f),
                        Black
                    ),
                    center = Offset(
                        x = 0.3f + offset1 * 0.4f,
                        y = 0.4f + offset2 * 0.3f
                    ),
                    radius = 800f + offset1 * 200f
                )
            )
    )
}

// ============================================================================
// Data Models
// ============================================================================

data class FogParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val alpha: Float,
    val speed: Float,
    val phase: Float
)

@Composable
private fun FogParticles(started: Boolean) {
    val particles = remember {
        List(15) {
            FogParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 150f + 100f,
                alpha = Random.nextFloat() * 0.1f + 0.05f,
                speed = Random.nextFloat() * 0.3f + 0.1f,
                phase = Random.nextFloat() * 360f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "fog")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fog time"
    )

    val fogAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 200),
        label = "fog alpha"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(40.dp)
    ) {
        particles.forEach { particle ->
            val offsetX = sin((time + particle.phase) * 0.01f * particle.speed) * 100f
            val offsetY = sin((time + particle.phase + 90f) * 0.008f * particle.speed) * 80f

            drawCircle(
                color = Platinum,
                radius = particle.size,
                center = Offset(
                    x = size.width * particle.x + offsetX,
                    y = size.height * particle.y + offsetY
                ),
                alpha = particle.alpha * fogAlpha
            )
        }
    }
}

@Composable
private fun FloatingCardSilhouettes(started: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "cards")

    val card1Offset by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "card1"
    )

    val card2Offset by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "card2"
    )

    val cardAlpha by animateFloatAsState(
        targetValue = if (started) 0.03f else 0f,
        animationSpec = tween(800),
        label = "card alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Card silhouette 1
        drawRect(
            color = Platinum,
            topLeft = Offset(size.width * card1Offset - 100f, size.height * 0.2f),
            size = androidx.compose.ui.geometry.Size(200f, 280f),
            alpha = cardAlpha
        )

        // Card silhouette 2
        drawRect(
            color = Gold,
            topLeft = Offset(size.width * card2Offset - 100f, size.height * 0.6f),
            size = androidx.compose.ui.geometry.Size(200f, 280f),
            alpha = cardAlpha * 0.5f
        )
    }
}

// ============================================================================
// Hero Imagery - Playing Cards Fan
// ============================================================================

@Composable
private fun PlayingCardsFan(started: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "cards fan")

    // Gentle floating effect for entire fan
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float Y"
    )

    // Subtle rotation oscillation
    val fanRotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fan rotation"
    )

    // Pulsing glow
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val fanAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(600, delayMillis = 400),
        label = "fan alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { clip = false }
            .padding(bottom = 350.dp),
        contentAlignment = Alignment.Center
    ) {
        // Golden glow behind
        Canvas(
            modifier = Modifier
                .size(350.dp)
                .alpha(fanAlpha * glow)
                .blur(35.dp)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = 0.8f),
                        Gold.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                radius = size.minDimension / 2
            )
        }

        // Energy particles orbiting the fan
        EnergyParticles(started, fanAlpha)

        // Playing cards fan
        Box(
            modifier = Modifier
                .size(280.dp)
                .graphicsLayer {
                    translationY = floatY
                    rotationZ = fanRotation
                    clip = false
                }
        ) {
            // Define 4 cards with different suits and positions
            val cards = listOf(
                CardData(Res.drawable.ic_spades, -24f, 0, Color.Black),
                CardData(Res.drawable.ic_hearts, -8f, 150, Color(0xFFDC143C)),
                CardData(Res.drawable.ic_clubs, 8f, 300, Color.Black),
                CardData(Res.drawable.ic_diamond, 24f, 450, Gold)
            )

            cards.forEach { card ->
                AnimatedCard(
                    suit = card.suit,
                    rotation = card.rotation,
                    started = started,
                    delayMillis = card.delay,
                    suitColor = card.color,
                    alpha = fanAlpha
                )
            }
        }
    }
}

data class CardData(
    val suit: DrawableResource,
    val rotation: Float,
    val delay: Int,
    val color: Color
)

data class DustParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val alpha: Float,
    val speed: Float
)

@Composable
private fun AnimatedCard(
    suit: DrawableResource,
    rotation: Float,
    started: Boolean,
    delayMillis: Int,
    suitColor: Color,
    alpha: Float
) {
    // Entry animation with staggered timing
    val scale by animateFloatAsState(
        targetValue = if (started) 1f else 0.7f,
        animationSpec = tween(600, delayMillis = delayMillis, easing = FastOutSlowInEasing),
        label = "card scale"
    )

    val cardRotation by animateFloatAsState(
        targetValue = if (started) rotation else 0f,
        animationSpec = tween(700, delayMillis = delayMillis, easing = FastOutSlowInEasing),
        label = "card rotation"
    )

    // Individual card breathing
    val infiniteTransition = rememberInfiniteTransition(label = "card breath")
    val breathe by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + delayMillis % 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMillis)
        ),
        label = "breathe"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { clip = false },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(90.dp, 130.dp)
                .graphicsLayer {
                    rotationZ = cardRotation
                    scaleX = scale * breathe
                    scaleY = scale * breathe
                    this.alpha = alpha
                    clip = false
                }
        ) {
            val cornerRadius = 12.dp.toPx()

            // Card shadow
            drawRoundRect(
                color = Black.copy(alpha = 0.3f),
                topLeft = Offset(4f, 6f),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
            )

            // Card background with gradient
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GhostWhite,
                        Platinum
                    )
                ),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
            )

            // Golden border
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Gold.copy(alpha = 0.8f),
                        Champagne,
                        Gold.copy(alpha = 0.6f)
                    )
                ),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )

            // Inner border (black)
            drawRoundRect(
                color = Black.copy(alpha = 0.15f),
                topLeft = Offset(3f, 3f),
                size = androidx.compose.ui.geometry.Size(size.width - 6f, size.height - 6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius - 3f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
            )
        }

        // Suit icon on card
        androidx.compose.material3.Icon(
            painter = org.jetbrains.compose.resources.painterResource(suit),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer {
                    rotationZ = cardRotation
                    scaleX = scale * breathe
                    scaleY = scale * breathe
                    this.alpha = alpha
                    clip = false
                },
            tint = suitColor
        )
    }
}

@Composable
private fun EnergyParticles(started: Boolean, baseAlpha: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "energy")

    val orbitalAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbital"
    )

    val particleAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(800, delayMillis = 1200),
        label = "particle alpha"
    )

    Canvas(modifier = Modifier.size(300.dp)) {
        for (i in 0..7) {
            val angle = (orbitalAngle + i * 45f) * (kotlin.math.PI / 180f).toFloat()
            val radius = 120f + sin(orbitalAngle * 0.05f + i) * 15f
            val x = center.x + kotlin.math.cos(angle) * radius
            val y = center.y + sin(angle) * radius * 0.7f

            drawCircle(
                color = Gold,
                radius = 3f + sin(orbitalAngle * 0.1f + i) * 1f,
                center = Offset(x, y),
                alpha = (0.6f + sin(orbitalAngle * 0.08f + i) * 0.3f) * particleAlpha * baseAlpha
            )
        }
    }
}

// ============================================================================
// Ambient Effects
// ============================================================================

@Composable
private fun AmbientGoldenDust(started: Boolean) {
    val particles = remember {
        List(25) {
            DustParticle(
                x = Random.nextFloat(),
                y = 1.0f + Random.nextFloat() * 0.2f,
                size = Random.nextFloat() * 2f + 1f,
                alpha = Random.nextFloat() * 0.4f + 0.2f,
                speed = Random.nextFloat() * 0.3f + 0.1f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "dust")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dust time"
    )

    val dustAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(1000, delayMillis = 2000),
        label = "dust alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val yPos = (particle.y - time * particle.speed) * size.height

            if (yPos in -50f..size.height + 50f) {
                val wobble = sin(time * 360f + particle.x * 100f) * 30f

                drawCircle(
                    color = Gold,
                    radius = particle.size,
                    center = Offset(
                        x = size.width * particle.x + wobble,
                        y = yPos
                    ),
                    alpha = particle.alpha * dustAlpha
                )
            }
        }
    }
}

// ============================================================================
// Content Layer - Text & Buttons
// ============================================================================

@Composable
private fun WelcomeContent(
    animationStarted: Boolean,
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // "Welcome to" text
        val welcomeAlpha by animateFloatAsState(
            targetValue = if (animationStarted) 1f else 0f,
            animationSpec = tween(600, delayMillis = 600),
            label = "welcome alpha"
        )

        Text(
            text = stringResource(Res.string.welcome_to),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Light,
                color = GhostWhite
            ),
            modifier = Modifier.alpha(welcomeAlpha)
        )

        Spacer(Modifier.height(Dimens.grid_0_5))

        // Animated logo reveal
        AnimatedLogoReveal(animationStarted)

        Spacer(Modifier.height(Dimens.grid_1))

        // Tagline
        val taglineAlpha by animateFloatAsState(
            targetValue = if (animationStarted) 1f else 0f,
            animationSpec = tween(800, delayMillis = 1400),
            label = "tagline alpha"
        )

        val taglinePulse by rememberInfiniteTransition(label = "tagline pulse")
            .animateFloat(
                initialValue = 0.7f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(2000)
                ),
                label = "pulse"
            )

        Text(
            text = stringResource(Res.string.select_option_to_start),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                color = Platinum.copy(alpha = taglinePulse),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.alpha(taglineAlpha)
        )

        Spacer(Modifier.height(Dimens.grid_2))

        // Buttons with staggered appearance
        val button1Alpha by animateFloatAsState(
            targetValue = if (animationStarted) 1f else 0f,
            animationSpec = tween(600, delayMillis = 1600),
            label = "button1 alpha"
        )

        val button2Alpha by animateFloatAsState(
            targetValue = if (animationStarted) 1f else 0f,
            animationSpec = tween(600, delayMillis = 1800),
            label = "button2 alpha"
        )

        EnhancedCinematicButton(
            text = stringResource(Res.string.create_account),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(button1Alpha),
            onClick = onCreateAccountClick,
            isPrimary = false,
            animationStarted = animationStarted
        )

        Spacer(Modifier.height(Dimens.grid_3))

        EnhancedCinematicButton(
            text = stringResource(Res.string.sign_in),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(button2Alpha),
            onClick = onSignInClick,
            isPrimary = true,
            animationStarted = animationStarted
        )
    }
}

@Composable
private fun AnimatedLogoReveal(started: Boolean) {
    val text = stringResource(Res.string.app_name)

    // Shimmer effect
    val infiniteTransition = rememberInfiniteTransition(label = "logo shimmer")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(1200)
        ),
        label = "shimmer"
    )

    val logoScale by animateFloatAsState(
        targetValue = if (started) 1f else 0.9f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logo scale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(800, delayMillis = 800),
        label = "logo alpha"
    )

    Box(
        modifier = Modifier.graphicsLayer {
            scaleX = logoScale
            scaleY = logoScale
            alpha = logoAlpha
        }
    ) {
        // Base text
        Text(
            text = text,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
            color = Platinum
        )

        // Gold shimmer overlay
        if (started && shimmer in 0f..1f) {
            Text(
                text = text,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = Gold,
                modifier = Modifier.alpha(
                    (1f - kotlin.math.abs(shimmer - 0.5f) * 2f)
                        .coerceIn(0f, 0.8f)
                )
            )
        }
    }
}

// ============================================================================
// Enhanced CTA Buttons
// ============================================================================

@Composable
private fun EnhancedCinematicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    animationStarted: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button animation")

    // Pulsing glow effect
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Shimmer sweep effect
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(if (isPrimary) 500 else 0)
        ),
        label = "shimmer"
    )

    // Scale pulse for "breathing" effect
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = if (animationStarted) breathScale else 1f
                scaleY = if (animationStarted) breathScale else 1f
            },
        contentAlignment = Alignment.Center
    ) {
        // Glowing aura behind button
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .blur(20.dp)
        ) {
            drawRoundRect(
                brush = if (isPrimary) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Gold.copy(alpha = 0.4f * glowIntensity),
                            Champagne.copy(alpha = 0.5f * glowIntensity),
                            Gold.copy(alpha = 0.4f * glowIntensity)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Platinum.copy(alpha = 0.3f * glowIntensity),
                            GhostWhite.copy(alpha = 0.4f * glowIntensity),
                            Platinum.copy(alpha = 0.3f * glowIntensity)
                        )
                    )
                },
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
            )
        }

        // Main button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .background(
                    brush = if (isPrimary) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                Gold.copy(alpha = 0.9f),
                                Champagne.copy(alpha = 0.95f),
                                Gold.copy(alpha = 0.9f)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                Platinum.copy(alpha = 0.15f),
                                GhostWhite.copy(alpha = 0.2f),
                                Platinum.copy(alpha = 0.15f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(28.dp)
                )
                .then(
                    if (!isPrimary) {
                        Modifier.border(
                            width = 2.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Platinum.copy(alpha = 0.5f),
                                    GhostWhite.copy(alpha = 0.7f),
                                    Platinum.copy(alpha = 0.5f)
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            // Shimmer overlay
            if (animationStarted && shimmerOffset in -0.5f..1.5f) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val shimmerWidth = size.width * 0.3f
                    val shimmerX = size.width * shimmerOffset

                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                GhostWhite.copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            startX = shimmerX - shimmerWidth / 2,
                            endX = shimmerX + shimmerWidth / 2
                        )
                    )
                }
            }

            // Button text
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 15.sp,
                    color = if (isPrimary) Black else GhostWhite
                )
            )
        }
    }
}

// ============================================================================
// Preview
// ============================================================================

@Preview
@Composable
private fun GetStartedScreenPreview() = SurfacePreview {
    GetStartedScreen(
        onSignInClick = {},
        onCreateAccountClick = {}
    )
}
