package com.ghn.poker.tracker.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Deck
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.ui.cards.EquityCalculator
import com.ghn.poker.tracker.ui.cards.PlayerCard
import com.ghn.poker.tracker.ui.theme.GizmoTheme

@Preview(showBackground = true)
@Composable
private fun CardPreview() = GizmoTheme {
    Surface(Modifier.padding(8.dp)) {
        val card = Deck.cards.random()
        PlayerCard(card = card, cardSize = CardSize.EXTRA_EXTRA_SMALL)
    }
}

@Preview(showBackground = true)
@Composable
private fun EquityCalculatorPreview() = GizmoTheme {
    Surface(Modifier.padding(8.dp)) {
        EquityCalculator()
    }
}
