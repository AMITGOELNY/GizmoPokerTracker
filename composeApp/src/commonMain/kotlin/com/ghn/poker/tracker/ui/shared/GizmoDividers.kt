package com.ghn.poker.tracker.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.ui.theme.GizmoTheme
import com.ghn.poker.tracker.ui.theme.Obsidian
import com.ghn.poker.tracker.ui.theme.Onyx
import com.ghn.poker.tracker.ui.theme.Platinum
import com.ghn.poker.tracker.ui.theme.Silver

/**
 * Subtle vertical divider
 */
@Composable
fun GizmoDividerVertical(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
) {
    Box(
        modifier = modifier
            .size(width = 1.dp, height = height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Onyx,
                        Color.Transparent
                    )
                )
            )
    )
}

/**
 * Subtle horizontal divider
 */
@Composable
fun GizmoDividerHorizontal(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Onyx,
                        Color.Transparent
                    )
                )
            )
    )
}

@Preview
@Composable
private fun GizmoDividerVerticalPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Vertical Dividers",
                    style = MaterialTheme.typography.titleMedium,
                    color = Platinum
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Item 1", color = Silver)
                    GizmoDividerVertical()
                    Text("Item 2", color = Silver)
                    GizmoDividerVertical(height = 32.dp)
                    Text("Item 3", color = Silver)
                    GizmoDividerVertical(height = 64.dp)
                    Text("Item 4", color = Silver)
                }
            }
        }
    }
}

@Preview
@Composable
private fun GizmoDividerHorizontalPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Horizontal Dividers",
                    style = MaterialTheme.typography.titleMedium,
                    color = Platinum
                )

                Text("Section 1", color = Silver)
                GizmoDividerHorizontal()
                Text("Section 2", color = Silver)
                GizmoDividerHorizontal()
                Text("Section 3", color = Silver)
            }
        }
    }
}

@Preview
@Composable
private fun GizmoDividersInContextPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(modifier = Modifier.padding(16.dp)) {
                GizmoCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Card with Dividers",
                            style = MaterialTheme.typography.titleMedium,
                            color = Platinum
                        )

                        GizmoDividerHorizontal(
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Value 1", style = MaterialTheme.typography.titleLarge, color = Platinum)
                                Text("Label 1", style = MaterialTheme.typography.bodySmall, color = Silver)
                            }
                            GizmoDividerVertical()
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Value 2", style = MaterialTheme.typography.titleLarge, color = Platinum)
                                Text("Label 2", style = MaterialTheme.typography.bodySmall, color = Silver)
                            }
                            GizmoDividerVertical()
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Value 3", style = MaterialTheme.typography.titleLarge, color = Platinum)
                                Text("Label 3", style = MaterialTheme.typography.bodySmall, color = Silver)
                            }
                        }
                    }
                }
            }
        }
    }
}
