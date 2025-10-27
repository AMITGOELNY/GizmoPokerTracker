package com.ghn.poker.tracker.ui.tracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.magic_city_casino
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun SessionListItem(session: SessionData) {
    val profitValue = (session.endAmount?.toDoubleOrNull() ?: 0.0) - (session.startAmount?.toDoubleOrNull() ?: 0.0)
    val isProfit = profitValue >= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.grid_2),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2438).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = if (isProfit) {
                            listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        } else {
                            listOf(
                                Color(0xFFEF5350).copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        }
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = if (isProfit) {
                            listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.3f),
                                Color(0xFF4CAF50).copy(alpha = 0.1f)
                            )
                        } else {
                            listOf(
                                Color(0xFFEF5350).copy(alpha = 0.3f),
                                Color(0xFFEF5350).copy(alpha = 0.1f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(Dimens.grid_2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Venue and Date
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Venue
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Date
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = session.formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(Modifier.width(Dimens.grid_1))

                // Right side - Profit/Loss
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Profit/Loss indicator with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isProfit) {
                                        Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    } else {
                                        Color(0xFFEF5350).copy(alpha = 0.2f)
                                    }
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
                                tint = if (isProfit) Color(0xFF4CAF50) else Color(0xFFEF5350),
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = session.netProfit,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (isProfit) Color(0xFF4CAF50) else Color(0xFFEF5350)
                        )
                    }

                    // Buy-in and Cash-out
                    Text(
                        text = "${session.startAmount} → ${session.endAmount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
