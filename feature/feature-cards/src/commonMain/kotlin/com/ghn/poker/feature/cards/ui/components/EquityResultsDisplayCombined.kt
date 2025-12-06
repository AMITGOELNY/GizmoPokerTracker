package com.ghn.poker.feature.cards.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.core.ui.preview.SurfacePreview
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.feature.cards.presentation.HandResults
import gizmopoker.feature.feature_cards.generated.resources.Res
import gizmopoker.feature.feature_cards.generated.resources.hero
import gizmopoker.feature.feature_cards.generated.resources.percent
import gizmopoker.feature.feature_cards.generated.resources.villain
import gizmopoker.feature.feature_cards.generated.resources.vs
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun EquityResultsDisplayCombined(results: HandResults) {
    val infiniteTransition = rememberInfiniteTransition()

    // Main animation cycle (0 to 1, repeats every 20 seconds)
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Continuous pulse between animations
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animation phases:
    // 0.000 - 0.125 (0-2.5s): Count-up animation with progress rings
    // 0.125 - 0.175 (2.5-3.5s): Energy burst from center
    // 0.175 - 0.325 (3.5-6.5s): Victory celebration with confetti
    // 0.325 - 1.000 (6.5-20s): Idle with gentle pulse

    val isCountingUp = animationProgress < 0.125f
    val isEnergyBurst = animationProgress in 0.125f..<0.175f
    val isCelebrating = animationProgress in 0.175f..<0.325f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .rotate(if (isCelebrating) sin(animationProgress * 60f) * 1.5f else 0f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2438).copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.1f),
                            Color(0xFF7B4FFF).copy(alpha = 0.05f),
                            Color(0xFF5265FF).copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.5f),
                            Color(0xFF7B4FFF).copy(alpha = 0.3f),
                            Color(0xFF5265FF).copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = Dimens.grid_2, horizontal = Dimens.grid_2)
        ) {
            // Combined canvas for particles and confetti
            Canvas(modifier = Modifier.matchParentSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2

                // Energy burst particles (2.5-3.5s)
                if (isEnergyBurst) {
                    val burstProgress = (animationProgress - 0.125f) / 0.05f // 0 to 1 over burst phase
                    val particleCount = 12

                    for (i in 0 until particleCount) {
                        val angle = (i.toFloat() / particleCount) * 2 * kotlin.math.PI
                        val distance = 150f * burstProgress
                        val x = centerX + (cos(angle) * distance).toFloat()
                        val y = centerY + (sin(angle) * distance).toFloat()
                        val alpha = 1f - burstProgress

                        drawCircle(
                            color = Color(0xFF5265FF).copy(alpha = alpha * 0.8f),
                            radius = 5f,
                            center = Offset(x, y)
                        )

                        // Add sparkle trail
                        drawCircle(
                            color = Color.White.copy(alpha = alpha * 0.4f),
                            radius = 2f,
                            center = Offset(x, y)
                        )
                    }
                }

                // Confetti celebration (3.5-6.5s)
                if (isCelebrating) {
                    val celebrationProgress = (animationProgress - 0.175f) / 0.15f // 0 to 1 over celebration phase
                    val confettiCount = 25

                    for (i in 0 until confettiCount) {
                        val seed = i * 123
                        val xPos = (size.width * ((seed % 100) / 100f))
                        val yPos = size.height * celebrationProgress
                        val color = when (seed % 5) {
                            0 -> Color(0xFFFFD700)
                            1 -> Color(0xFF5265FF)
                            2 -> Color(0xFFE21221)
                            3 -> Color(0xFF4CAF50)
                            else -> Color(0xFFFF6B9D)
                        }

                        drawRect(
                            color = color.copy(alpha = 1f - celebrationProgress),
                            topLeft = Offset(xPos, yPos),
                            size = Size(6f, 10f)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val countUpProgress = if (isCountingUp) animationProgress * 8f else 1f
                val bounceScale = if (isCelebrating) {
                    1f + sin(animationProgress * 40f) * 0.15f
                } else if (!isCountingUp && !isEnergyBurst) {
                    pulseScale
                } else {
                    1f
                }

                PlayerResultColumnCombined(
                    targetPercentage = results.winPercent,
                    label = stringResource(Res.string.hero),
                    primaryColor = Color(0xFF4CAF50),
                    secondaryColor = Color(0xFF66BB6A),
                    progress = countUpProgress,
                    isAnimating = isCountingUp,
                    scale = bounceScale
                )

                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .scale(
                            when {
                                isEnergyBurst -> 1f + ((animationProgress - 0.125f) / 0.05f) * 0.3f
                                isCelebrating -> bounceScale
                                else -> 1f
                            }
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = if (isEnergyBurst || isCelebrating) 0.8f else 0.5f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.vs),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                PlayerResultColumnCombined(
                    targetPercentage = results.lossPercent,
                    label = stringResource(Res.string.villain),
                    primaryColor = Color(0xFFE21221),
                    secondaryColor = Color(0xFFFF5252),
                    progress = countUpProgress,
                    isAnimating = isCountingUp,
                    scale = bounceScale
                )
            }
        }
    }
}

@Composable
private fun PlayerResultColumnCombined(
    targetPercentage: String,
    label: String,
    primaryColor: Color,
    secondaryColor: Color,
    progress: Float,
    isAnimating: Boolean,
    scale: Float = 1f
) {
    val targetValue = targetPercentage.toFloatOrNull() ?: 0f
    val currentValue = targetValue * progress

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_0_75)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            // Progress ring (visible during count-up)
            if (isAnimating) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 4.dp.toPx()
                    val diameter = size.minDimension - strokeWidth

                    // Background ring
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress arc
                    val sweepAngle = 360f * progress
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(primaryColor, secondaryColor, primaryColor)
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            // Circle with percentage
            Box(
                modifier = Modifier
                    .size(if (isAnimating) 62.dp else 70.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.3f),
                                primaryColor.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(primaryColor, secondaryColor)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isAnimating) {
                        val roundedValue = (currentValue * 10).roundToInt() / 10.0
                        "$roundedValue${stringResource(Res.string.percent)}"
                    } else {
                        "$targetPercentage${stringResource(Res.string.percent)}"
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        brush = Brush.linearGradient(
                            colors = listOf(primaryColor, secondaryColor)
                        ),
                        fontSize = if (isAnimating) 16.sp else 18.sp
                    )
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = primaryColor
        )
    }
}

@Preview
@Composable
private fun EquityResultsDisplayCombinedPreview() = SurfacePreview {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0E1A), Color(0xFF131629))
                )
            )
            .padding(Dimens.grid_2_5)
    ) {
        EquityResultsDisplayCombined(
            results = HandResults(winPercent = "65.5", lossPercent = "34.5", tiePercent = "0")
        )
    }
}
