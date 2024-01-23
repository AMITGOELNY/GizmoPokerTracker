package com.ghn.poker.tracker.ui.cards

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.presentation.cards.CardScreenActions
import com.ghn.poker.tracker.presentation.cards.CardScreenViewModel
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.presentation.cards.model.color
import com.ghn.poker.tracker.presentation.cards.model.drawable
import com.ghn.poker.tracker.ui.theme.Dimens
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun CardsScreen(viewModel: CardScreenViewModel = koinInject()) {
    val state = viewModel.state.collectAsState().value
    var started by remember { mutableStateOf(false) }
    val cardSize by remember { mutableStateOf(CardSize.EXTRA_SMALL) }
    val transition = updateTransition(started, label = "selected state")
    val alpha by transition.animateFloat(label = "elevation") { isSelected ->
        if (isSelected) 1f else .4f
    }
//    val borderColor by transition.animateColor(label = "border color") { isSelected ->
//        if (isSelected) Color.Magenta else Color.White
//    }

    val card1Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) -140f else 0f
    }
    val card2Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) -70f else 0f
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
        if (isSelected) 70f else 0f
    }
    val card5Translation by transition.animateFloat(
        label = "elevation",
        transitionSpec = { tween(400, easing = LinearEasing) }
    ) { isSelected ->
        if (isSelected) 140f else 0f
    }

    LaunchedEffect(Unit) {
        delay(200)
        started = true
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            state.playerCard.forEach { playerCards ->
                Box(contentAlignment = Alignment.Center) {
                    playerCards.forEachIndexed { index, card ->
                        Column(
                            Modifier.graphicsLayer(
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

        SmallFloatingActionButton(
            onClick = { viewModel.dispatch(CardScreenActions.NewGame) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(Dimens.grid_2_5),
            containerColor = Color(0xFFc42727)
        ) {
            Text("+")
        }
    }
}

@Composable
internal fun PlayerCard(card: Card, cardSize: CardSize) {
    Box(
        Modifier.width(cardSize.width)
            .aspectRatio(.66f)
            .background(Color.White, cardSize.shape)
            .border(.3.dp, Color.Gray, cardSize.shape)
            .padding(horizontal = cardSize.paddingValues, vertical = Dimens.grid_0_5)
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
@OptIn(ExperimentalResourceApi::class)
private fun CornerCard(
    card: Card,
    cardSize: CardSize,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            card.name,
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
