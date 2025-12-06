package com.ghn.poker.feature.cards.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Card
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.feature.cards.presentation.model.CardSize
import com.ghn.poker.feature.cards.presentation.model.color
import com.ghn.poker.feature.cards.presentation.model.drawable
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun PlayerCard(
    card: Card,
    cardSize: CardSize,
    disabled: Boolean = false,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        Modifier.width(cardSize.width)
            .aspectRatio(.66f)
            .background(
                color = if (!disabled || selected) Color.White else Color.White.copy(alpha = .3f),
                shape = cardSize.shape
            )
            .border(
                width = if (selected) 2.dp else .3.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = cardSize.shape
            )
            .padding(horizontal = cardSize.paddingValues, vertical = Dimens.grid_0_5)
            .clickable(enabled = onClick != null && !disabled, onClick = { onClick?.invoke() })
    ) {
        CornerCard(card, cardSize)
        CornerCard(
            card = card,
            cardSize = cardSize,
            modifier = Modifier.align(Alignment.BottomEnd).rotate(180f)
        )
    }
}

@Composable
private fun CornerCard(
    card: Card,
    cardSize: CardSize,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = card.name,
            color = card.suit.color,
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = cardSize.fontSize,
                lineHeight = cardSize.lineHeight,
                fontWeight = FontWeight.SemiBold
            ),
        )
        Image(
            painter = painterResource(card.suit.drawable),
            contentDescription = null,
            modifier = Modifier.size(cardSize.iconSize)
        )
    }
}
