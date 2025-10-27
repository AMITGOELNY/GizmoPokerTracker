package com.ghn.poker.tracker.ui.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import com.ghn.poker.tracker.presentation.cards.CardScreenActions
import com.ghn.poker.tracker.presentation.cards.CardScreenViewModel
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.plus
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun FiveCardGame(
    viewModel: CardScreenViewModel = koinInject()
) {
    val state = viewModel.state.collectAsState().value
    var started by remember { mutableStateOf(false) }
    val cardSize by remember { mutableStateOf(CardSize.EXTRA_EXTRA_SMALL) }
    val transition = updateTransition(started, label = "selected state")
    val alpha by transition.animateFloat(label = "elevation") { isSelected ->
        if (isSelected) 1f else 1f
    }

    val card1Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) -130f else 0f
    }
    val card2Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) -65f else 0f
    }
    val card3Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) 0f else 0f
    }
    val card4Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) 65f else 0f
    }
    val card5Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) 130f else 0f
    }
    LaunchedEffect(state) {
        delay(200)
        started = true
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            state.playerCard.forEach { playerCards ->
                Box(contentAlignment = Alignment.Center) {
                    playerCards.forEachIndexed { index, card ->
                        Column(
                            Modifier.padding(bottom = Dimens.grid_2)
                                .graphicsLayer(
                                    translationX = when (index) {
                                        0 -> card1Translation
                                        1 -> card2Translation
                                        2 -> card3Translation
                                        3 -> card4Translation
                                        else -> card5Translation
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

        AnimatedContent(
            targetState = state.winnerInfo,
        ) { state ->
            if (state != null) {
                Text(
                    "Player${state.winnerIndex + 1} wins with\n${state.winningHand}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    textAlign = TextAlign.Center
                )
            }
        }

        SmallFloatingActionButton(
            onClick = {
                started = false
                viewModel.dispatch(CardScreenActions.NewGame)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(Dimens.grid_2_5),
            containerColor = Color(0xFFc42727)
        ) {
            Text(stringResource(Res.string.plus))
        }
    }
}
