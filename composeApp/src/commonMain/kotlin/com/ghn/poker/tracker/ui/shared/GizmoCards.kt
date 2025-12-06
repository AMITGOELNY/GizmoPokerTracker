package com.ghn.poker.tracker.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.ui.theme.ChampagneGold
import com.ghn.poker.tracker.ui.theme.Emerald
import com.ghn.poker.tracker.ui.theme.GizmoShapes
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.theme.Graphite
import com.ghn.poker.tracker.ui.theme.Obsidian
import com.ghn.poker.tracker.ui.theme.Onyx
import com.ghn.poker.tracker.ui.theme.Platinum
import com.ghn.poker.tracker.ui.theme.Ruby
import com.ghn.poker.tracker.ui.theme.Silver
import com.ghn.poker.tracker.ui.theme.Slate

/**
 * Standard elevated card with subtle gradient
 */
@Composable
fun GizmoCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = GizmoShapes.sessionCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick ?: {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Graphite.copy(alpha = 0.8f),
                            Slate.copy(alpha = 0.6f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Onyx.copy(alpha = 0.5f),
                    shape = GizmoShapes.sessionCard
                )
        ) {
            content()
        }
    }
}

/**
 * Stats card with accent indicator
 */
@Composable
fun GizmoStatsCard(
    modifier: Modifier = Modifier,
    accentColor: Color = ChampagneGold,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = GizmoShapes.sessionCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            Graphite.copy(alpha = 0.6f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.4f),
                            Onyx.copy(alpha = 0.3f)
                        )
                    ),
                    shape = GizmoShapes.sessionCard
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun GizmoCardPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(modifier = Modifier.padding(16.dp)) {
                GizmoCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Session Card",
                            style = MaterialTheme.typography.titleMedium,
                            color = Platinum
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This is a standard card with gradient background",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Silver
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GizmoStatsCardPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(modifier = Modifier.padding(16.dp)) {
                GizmoStatsCard(
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = ChampagneGold
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Gold Accent Stats",
                            style = MaterialTheme.typography.titleMedium,
                            color = Platinum
                        )
                        Text(
                            text = "$1,250",
                            style = MaterialTheme.typography.headlineLarge,
                            color = ChampagneGold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                GizmoStatsCard(
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = Emerald
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Profit",
                            style = MaterialTheme.typography.titleMedium,
                            color = Platinum
                        )
                        Text(
                            text = "+$450",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Emerald
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                GizmoStatsCard(
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = Ruby
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Loss",
                            style = MaterialTheme.typography.titleMedium,
                            color = Platinum
                        )
                        Text(
                            text = "-$200",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Ruby
                        )
                    }
                }
            }
        }
    }
}
