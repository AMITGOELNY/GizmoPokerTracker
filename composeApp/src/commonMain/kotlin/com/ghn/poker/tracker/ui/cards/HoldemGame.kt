package com.ghn.poker.tracker.ui.cards

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.presentation.cards.CardScreenHoldEmActions
import com.ghn.poker.tracker.presentation.cards.CardScreenHoldEmViewModel
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.ui.theme.Dimens
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun HoldemGame(
    viewModel: CardScreenHoldEmViewModel = koinInject()
) {
    val state = viewModel.state.collectAsState().value
    var started by remember { mutableStateOf(false) }
    val cardSize by remember { mutableStateOf(CardSize.EXTRA_SMALL) }
    val transition = updateTransition(started, label = "selected state")
    val alpha by transition.animateFloat(label = "elevation") { isSelected ->
        if (isSelected) 1f else 1f
    }

    val card1Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) -50f else 0f
    }
    val card2Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) 50f else 0f
    }

    LaunchedEffect(state) {
        delay(200)
        started = true
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(Modifier.fillMaxWidth()) {
            Column((Modifier.fillMaxWidth(.3f))) {
                state.playerCard.forEach { playerCards ->
                    Box(
                        modifier = Modifier.padding(horizontal = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        playerCards.forEachIndexed { index, card ->
                            Column(
                                Modifier.padding(bottom = Dimens.grid_2)
                                    .graphicsLayer(
                                        translationX = when (index) {
                                            0 -> card1Translation
                                            else -> card2Translation
                                        },
                                        alpha = alpha
                                    )
                            ) {
                                PlayerCard(card, cardSize)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                state.boardCards.forEachIndexed { _, card ->
                    PlayerCard(card, cardSize)
                }
            }
        }

        SmallFloatingActionButton(
            onClick = {
                started = false
                viewModel.dispatch(CardScreenHoldEmActions.NewGame)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(Dimens.grid_2_5),
            containerColor = Color(0xFFc42727)
        ) {
            Text("+")
        }
    }
}
