package com.ghn.poker.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.Emerald
import com.ghn.poker.core.ui.theme.GizmoTheme
import com.ghn.poker.core.ui.theme.Obsidian
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Single stat item display
 */
@Composable
fun GizmoStatItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Platinum,
    iconColor: Color = ChampagneGold,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Silver
        )
    }
}

@Preview
@Composable
private fun GizmoStatItemPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Individual Stats",
                    style = MaterialTheme.typography.titleMedium,
                    color = Platinum
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    GizmoStatItem(
                        icon = Icons.Default.Casino,
                        label = "Sessions",
                        value = "42"
                    )
                    GizmoStatItem(
                        icon = Icons.Default.AccessTime,
                        label = "Hours",
                        value = "156"
                    )
                    GizmoStatItem(
                        icon = Icons.Default.AttachMoney,
                        label = "Buy-ins",
                        value = "$2,500"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GizmoStatItemColorsPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Colored Stats",
                    style = MaterialTheme.typography.titleMedium,
                    color = Platinum
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    GizmoStatItem(
                        icon = Icons.Default.TrendingUp,
                        label = "Profit",
                        value = "+$1,250",
                        valueColor = Emerald,
                        iconColor = Emerald
                    )
                    GizmoStatItem(
                        icon = Icons.Default.TrendingUp,
                        label = "Loss",
                        value = "-$450",
                        valueColor = Ruby,
                        iconColor = Ruby
                    )
                    GizmoStatItem(
                        icon = Icons.Default.AttachMoney,
                        label = "Hourly",
                        value = "$25/hr",
                        valueColor = ChampagneGold,
                        iconColor = ChampagneGold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GizmoStatItemInCardPreview() {
    GizmoTheme {
        Surface(color = Obsidian) {
            Column(modifier = Modifier.padding(16.dp)) {
                GizmoStatsCard(accentColor = ChampagneGold) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GizmoStatItem(
                            icon = Icons.Default.Casino,
                            label = "Sessions",
                            value = "42"
                        )
                        GizmoDividerVertical()
                        GizmoStatItem(
                            icon = Icons.Default.AccessTime,
                            label = "Hours",
                            value = "156"
                        )
                        GizmoDividerVertical()
                        GizmoStatItem(
                            icon = Icons.Default.TrendingUp,
                            label = "Profit",
                            value = "+$1.2K",
                            valueColor = Emerald,
                            iconColor = Emerald
                        )
                    }
                }
            }
        }
    }
}
