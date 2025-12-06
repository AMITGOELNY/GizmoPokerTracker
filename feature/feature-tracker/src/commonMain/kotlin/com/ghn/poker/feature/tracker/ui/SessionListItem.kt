package com.ghn.poker.feature.tracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.ui.preview.SurfacePreview
import com.ghn.poker.core.ui.theme.ChampagneGold
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.core.ui.theme.Emerald
import com.ghn.poker.core.ui.theme.GizmoShapes
import com.ghn.poker.core.ui.theme.Graphite
import com.ghn.poker.core.ui.theme.Onyx
import com.ghn.poker.core.ui.theme.Platinum
import com.ghn.poker.core.ui.theme.Ruby
import com.ghn.poker.core.ui.theme.Silver
import com.ghn.poker.core.ui.theme.Slate
import com.ghn.poker.core.ui.theme.moneyDisplay
import com.ghn.poker.feature.tracker.domain.model.SessionData
import gizmopoker.feature.feature_tracker.generated.resources.Res
import gizmopoker.feature.feature_tracker.generated.resources.magic_city_casino
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@Composable
internal fun SessionListItem(session: SessionData) {
    val profitValue =
        (session.endAmount?.toDoubleOrNull() ?: 0.0) - (
            session.startAmount?.toDoubleOrNull()
                ?: 0.0
            )
    val isProfit = profitValue >= 0
    val accentColor = if (isProfit) Emerald else Ruby

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = GizmoShapes.sessionCard,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Graphite.copy(alpha = 0.7f),
                            Slate.copy(alpha = 0.5f)
                        )
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.25f),
                            Onyx.copy(alpha = 0.4f)
                        )
                    ),
                    shape = GizmoShapes.sessionCard
                )
                .padding(Dimens.grid_2)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top row - Venue and Profit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Venue
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            ChampagneGold.copy(alpha = 0.15f),
                                            ChampagneGold.copy(alpha = 0.03f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = ChampagneGold.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (session.venue == Venue.MAGIC_CITY) {
                            Image(
                                painter = painterResource(Res.drawable.magic_city_casino),
                                contentDescription = null,
                                modifier = Modifier.height(20.dp)
                            )
                        } else {
                            Text(
                                text = "Seminole Hard Rock",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = Platinum
                            )
                        }
                    }

                    // Profit/Loss indicator with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(
                                    width = 1.dp,
                                    color = accentColor.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isProfit) {
                                    Icons.AutoMirrored.Default.TrendingUp
                                } else {
                                    Icons.AutoMirrored.Default.TrendingDown
                                },
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = session.netProfit,
                            style = MaterialTheme.typography.moneyDisplay,
                            color = accentColor
                        )
                    }
                }

                // Bottom row - Date and Buy-in/Cash-out (aligned)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 46.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Silver.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = session.formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Silver
                        )
                    }

                    // Buy-in and Cash-out
                    Text(
                        text = "${session.startAmount} → ${session.endAmount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SessionListItemPreview() = SurfacePreview {
    SessionListItem(
        session = SessionData(
            date = Clock.System.now().minus(2.days),
            startAmount = "500",
            endAmount = "1250",
            venue = Venue.MAGIC_CITY
        )
    )
}

@Preview
@Composable
private fun SessionListItemLossPreview() = SurfacePreview {
    SessionListItem(
        session = SessionData(
            date = Clock.System.now().minus(5.days),
            startAmount = "1000",
            endAmount = "350",
            venue = Venue.HARD_ROCK_FL
        )
    )
}
