package com.ghn.poker.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.theme.AntiqueBrass
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.GizmoGradients
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Silver
import com.ghn.poker.core.ui.theme.Slate
import com.ghn.poker.core.ui.theme.buttonText
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Primary gold button - main CTAs
 */
@Composable
fun GizmoPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            loading -> 0.98f
            isPressed -> 0.97f
            else -> 1f
        },
        animationSpec = tween(100),
        label = "button scale"
    )

    val alpha = if (enabled && !loading) 1f else 0.6f

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        // Subtle glow behind button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .blur(16.dp)
                .alpha(if (enabled) 0.4f else 0.1f)
                .background(
                    brush = GizmoGradients.goldGlow,
                    shape = GizmoShapes.pill
                )
        )

        // Main button
        Surface(
            onClick = { if (enabled && !loading) onClick() },
            modifier = Modifier.fillMaxSize(),
            shape = GizmoShapes.pill,
            color = Color.Transparent,
            interactionSource = interactionSource,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AntiqueBrass.copy(alpha = alpha),
                                ChampagneGold.copy(alpha = alpha),
                                AntiqueBrass.copy(alpha = alpha)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Obsidian,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = text.uppercase(),
                        style = MaterialTheme.typography.buttonText,
                        color = Obsidian
                    )
                }
            }
        }
    }
}

/**
 * Secondary outlined button
 */
@Composable
fun GizmoSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "button scale"
    )

    val borderAlpha = if (enabled) 0.6f else 0.3f

    Surface(
        onClick = { if (enabled) onClick() },
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = GizmoShapes.pill,
        color = Color.Transparent,
        interactionSource = interactionSource,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Platinum.copy(alpha = borderAlpha * 0.5f),
                            Platinum.copy(alpha = borderAlpha),
                            Platinum.copy(alpha = borderAlpha * 0.5f)
                        )
                    ),
                    shape = GizmoShapes.pill
                )
                .background(
                    color = Slate.copy(alpha = 0.3f),
                    shape = GizmoShapes.pill
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.buttonText,
                color = if (enabled) Platinum else Silver
            )
        }
    }
}

/**
 * Icon button with subtle background
 */
@Composable
fun GizmoIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Platinum,
    backgroundColor: Color = Slate.copy(alpha = 0.5f),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(100),
        label = "icon button scale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = CircleShape,
        color = backgroundColor,
        interactionSource = interactionSource,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun GizmoPrimaryButtonPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GizmoPrimaryButton(
                    text = "Sign In",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                GizmoPrimaryButton(
                    text = "Loading",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    loading = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                GizmoPrimaryButton(
                    text = "Disabled",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun GizmoSecondaryButtonPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GizmoSecondaryButton(
                    text = "Cancel",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                GizmoSecondaryButton(
                    text = "Disabled",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun GizmoIconButtonPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GizmoIconButton(
                    icon = Icons.Default.Add,
                    onClick = {},
                    contentDescription = "Add"
                )
                Spacer(modifier = Modifier.height(16.dp))
                GizmoIconButton(
                    icon = Icons.Default.Settings,
                    onClick = {},
                    contentDescription = "Settings",
                    tint = ChampagneGold
                )
            }
        }
    }
}
