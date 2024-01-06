package com.ghn.poker.tracker.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.domain.usecase.netAmountColor
import com.ghn.poker.tracker.ui.theme.Dimens
import com.ghn.poker.tracker.ui.theme.title200

@Composable
internal fun SessionListItem(session: SessionData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.grid_2_5)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .border(
                width = 0.3.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .padding(
                vertical = Dimens.grid_2_5,
                horizontal = Dimens.grid_1_5
            ),
        verticalArrangement = Arrangement.spacedBy(
            space = Dimens.grid_1,
            alignment = Alignment.CenterVertically
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image(
//                painter = painterResource(res = "magic-city-casino.xml"),
//                contentDescription = null,
//                modifier = Modifier.height(18.dp)
//            )
            Text(session.netProfit, color = session.netAmountColor)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                session.formattedDate,
                style = MaterialTheme.typography.title200.copy(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }
    }
}
