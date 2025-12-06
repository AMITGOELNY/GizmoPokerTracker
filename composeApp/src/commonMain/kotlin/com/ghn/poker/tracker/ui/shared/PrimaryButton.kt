package com.ghn.poker.tracker.ui.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.ui.preview.SurfacePreview
import com.ghn.poker.tracker.ui.theme.AntiqueBrass
import com.ghn.poker.tracker.ui.theme.ChampagneGold
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.GizmoGradients
import com.ghn.poker.tracker.ui.theme.GizmoShapes
import com.ghn.poker.tracker.ui.theme.Obsidian
import com.ghn.poker.tracker.ui.theme.Platinum
import com.ghn.poker.tracker.ui.theme.Silver
import com.ghn.poker.tracker.ui.theme.Slate
import com.ghn.poker.tracker.ui.theme.buttonText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    showLoading: Boolean = false,
    fillMaxWidth: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    val scale by animateFloatAsState(
        targetValue = when {
            showLoading -> 0.98f
            else -> 1f
        },
        animationSpec = tween(150),
        label = "button scale"
    )

    val effectiveAlpha = if (isEnabled && !showLoading) 1f else 0.6f

    Box(
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .height(Dimens.grid_6_5)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        // Subtle glow behind button
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 4.dp)
                .blur(14.dp)
                .alpha(if (isEnabled) 0.35f else 0.1f)
                .background(
                    brush = GizmoGradients.goldGlow,
                    shape = GizmoShapes.pill
                )
        )

        // Main button
        Surface(
            onClick = { if (isEnabled && !showLoading) onClick() },
            modifier = Modifier.matchParentSize(),
            shape = RoundedCornerShape(Dimens.grid_2_5),
            color = Color.Transparent,
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AntiqueBrass.copy(alpha = effectiveAlpha),
                                ChampagneGold.copy(alpha = effectiveAlpha),
                                AntiqueBrass.copy(alpha = effectiveAlpha)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = showLoading,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "loading transition"
                ) { isLoading ->
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimens.grid_2_5),
                            color = Obsidian,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            content?.invoke(this)
                            Text(
                                text = buttonText,
                                style = MaterialTheme.typography.buttonText,
                                color = Obsidian
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SecondaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    showLoading: Boolean = false,
    isEnabled: Boolean = true,
    textColor: Color = Platinum,
    borderColor: Color = Silver.copy(alpha = 0.5f),
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    val borderAlpha = if (isEnabled) 0.6f else 0.3f

    Surface(
        onClick = { if (isEnabled && !showLoading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.grid_6_5),
        shape = RoundedCornerShape(Dimens.grid_3),
        color = Slate.copy(alpha = 0.3f),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Platinum.copy(alpha = borderAlpha * 0.4f),
                    Platinum.copy(alpha = borderAlpha),
                    Platinum.copy(alpha = borderAlpha * 0.4f)
                )
            )
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(Modifier.alpha(if (showLoading) 0f else 1f)) {
                content?.invoke(this)
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.buttonText,
                    color = if (isEnabled) textColor else textColor.copy(alpha = 0.5f)
                )
            }
            if (showLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(Dimens.grid_2_5),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryButton_Preview() = SurfacePreview {
    Column(Modifier.padding(vertical = Dimens.grid_2)) {
        PrimaryButton("Log in", Modifier.padding(horizontal = Dimens.grid_2), onClick = {})
    }
}

@Preview
@Composable
private fun SecondaryButton_Preview() = SurfacePreview {
    Column(Modifier.padding(vertical = Dimens.grid_2)) {
        SecondaryButton("Log in", Modifier.padding(horizontal = Dimens.grid_2), onClick = {})
    }
}
