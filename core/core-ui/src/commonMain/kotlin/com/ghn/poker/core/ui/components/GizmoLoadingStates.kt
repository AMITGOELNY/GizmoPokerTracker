package com.ghn.poker.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Premium loading animation
 */
@Composable
fun GizmoLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = ChampagneGold,
    size: Dp = 40.dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )

    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(alpha1, alpha2, alpha3).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(size / 3)
                    .alpha(alpha)
                    .background(color, CircleShape)
            )
        }
    }
}

/**
 * Empty state placeholder
 */
@Composable
fun GizmoEmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon with subtle glow
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ChampagneGold.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Onyx,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = ChampagneGold.copy(alpha = 0.7f)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Platinum
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Silver,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        action?.invoke()
    }
}

@Preview
@Composable
private fun GizmoLoadingIndicatorPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Default (Gold)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver
                )
                GizmoLoadingIndicator()

                Text(
                    text = "Large Size",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver
                )
                GizmoLoadingIndicator(size = 60.dp)

                Text(
                    text = "Custom Color",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver
                )
                GizmoLoadingIndicator(color = Platinum)
            }
        }
    }
}

@Preview
@Composable
private fun GizmoEmptyStatePreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                GizmoEmptyState(
                    icon = Icons.Default.Inbox,
                    title = "No Sessions Yet",
                    message = "Start tracking your poker sessions to see your stats here."
                )
            }
        }
    }
}

@Preview
@Composable
private fun GizmoEmptyStateWithActionPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            GizmoEmptyState(
                icon = Icons.Default.Search,
                title = "No Results Found",
                message = "Try adjusting your search or filters to find what you're looking for.",
                action = {
                    GizmoSecondaryButton(
                        text = "Clear Filters",
                        onClick = {}
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun GizmoEmptyStateErrorPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Ruby.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Ruby.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Ruby.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = "Something Went Wrong",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Platinum
                )

                Text(
                    text = "We couldn't load your data. Please try again.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Silver
                )

                GizmoPrimaryButton(
                    text = "Retry",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        }
    }
}
