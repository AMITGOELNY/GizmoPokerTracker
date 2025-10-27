package com.ghn.poker.tracker.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.ui.cards.EquityCalculator
import com.ghn.poker.tracker.ui.cards.PlayerCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun CardPreview() = SurfacePreview {
    Column(Modifier.padding(8.dp)) {
        val card = Deck.cards.random()
        PlayerCard(card = card, cardSize = CardSize.EXTRA_EXTRA_SMALL)
    }
}

@Preview
@Composable
private fun EquityCalculatorPreview() = SurfacePreview {
    Column(Modifier.padding(8.dp)) {
        EquityCalculator()
    }
}
