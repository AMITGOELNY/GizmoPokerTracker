package com.ghn.poker.tracker.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.poker.tracker.presentation.cards.CardRowType
import com.ghn.poker.tracker.ui.cards.CardRow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun CardRowPreview() = SurfacePreview {
    Column(Modifier.padding(16.dp)) {
        CardRow(
            board = CardRowType.HERO,
            cardsCat = listOf(
                Card(
                    suit = CardSuit.HEARTS,
                    name = "A",
                    value = 14
                ),
                Card(
                    suit = CardSuit.DIAMONDS,
                    name = "A",
                    value = 14
                )
            ),
            onClick = { _, _ -> }
        )
    }
}
