package com.ghn.poker.tracker.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.poker.tracker.presentation.cards.CardRowType
import com.ghn.poker.tracker.presentation.cards.HandResults
import com.ghn.poker.tracker.ui.cards.CardRow
import com.ghn.poker.tracker.ui.theme.GizmoTheme

@Preview(showBackground = true)
@Composable
private fun CardRowPreview() = SurfacePreview {
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
        results = HandResults(winPercent = "22.3", lossPercent = "22.3", tiePercent = "22.3"),
        onClick = { _, _ -> }
    )
}

@Composable
fun SurfacePreview(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    GizmoTheme { Surface(modifier) { content() } }
