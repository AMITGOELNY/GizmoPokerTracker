package com.ghn.poker.feature.cards.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghn.gizmodb.common.models.Card
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.feature.cards.presentation.CardScreenHoldEmAction
import com.ghn.poker.feature.cards.presentation.CardScreenHoldEmViewModel
import com.ghn.poker.feature.cards.presentation.model.CardSize
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun HoldemGame(
    viewModel: CardScreenHoldEmViewModel = koinInject()
) {
    val state = viewModel.state.collectAsState().value
    var cardsDealt by remember { mutableStateOf(false) }
    var showBoardCards by remember { mutableStateOf(false) }
    val cardSize by remember { mutableStateOf(CardSize.EXTRA_EXTRA_SMALL) }

    LaunchedEffect(state.playerCard) {
        if (state.playerCard.isNotEmpty()) {
            cardsDealt = false
            delay(300)
            cardsDealt = true
            delay(1500)
            showBoardCards = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF0D3818)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Container for table and players
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1.4f),
            contentAlignment = Alignment.Center
        ) {
            // Poker Table (smaller)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1.6f)
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(200.dp),
                        ambientColor = Color.Black.copy(alpha = 0.8f),
                        spotColor = Color.Black.copy(alpha = 0.8f)
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2E7D32),
                                Color(0xFF1B5E20)
                            )
                        ),
                        shape = RoundedCornerShape(200.dp)
                    )
                    .border(
                        width = 12.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF5D4037),
                                Color(0xFF3E2723),
                                Color(0xFF5D4037)
                            )
                        ),
                        shape = RoundedCornerShape(200.dp)
                    )
            ) {
                // Community Cards in Center
                Row(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.boardCards.forEachIndexed { index, card ->
                        AnimatedCommunityCard(
                            card = card,
                            cardSize = cardSize,
                            visible = showBoardCards,
                            delay = index * 150L
                        )
                    }
                }
            }

            // Player Positions Around Table (outside the table)
            // Top row: P4, P5, P6
            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(3) ?: emptyList(),
                position = "P4",
                modifier = Modifier.align(Alignment.TopStart),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 3,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.BOTTOM
            )

            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(4) ?: emptyList(),
                position = "P5",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(bottom = Dimens.grid_8),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 4,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.BOTTOM
            )

            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(5) ?: emptyList(),
                position = "P6",
                modifier = Modifier.align(Alignment.TopEnd),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 5,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.BOTTOM
            )

            // Bottom row: P3, P2, P1 (below P4, P5, P6 respectively)
            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(2) ?: emptyList(),
                position = "P3",
                modifier = Modifier.align(Alignment.BottomStart),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 2,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.TOP
            )

            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(1) ?: emptyList(),
                position = "P2",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = Dimens.grid_8),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 1,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.TOP
            )

            PlayerPositionWithIndicator(
                cards = state.playerCard.getOrNull(0) ?: emptyList(),
                position = "P1",
                modifier = Modifier.align(Alignment.BottomEnd),
                cardSize = cardSize,
                visible = cardsDealt,
                isWinner = state.winnerInfo?.winnerIndex == 0,
                winnerHand = state.winnerInfo?.winningHand,
                indicatorPosition = IndicatorPosition.TOP
            )
        }

        // New Hand Button
        Button(
            onClick = {
                cardsDealt = false
                showBoardCards = false
                viewModel.onDispatch(CardScreenHoldEmAction.NewGame)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Dimens.grid_3),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F)
            ),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "New Hand",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(Dimens.grid_1))
            Text(
                text = "New Hand",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private enum class IndicatorPosition {
    TOP,
    BOTTOM
}

@Composable
private fun PlayerPositionWithIndicator(
    cards: List<Card>,
    position: String,
    modifier: Modifier = Modifier,
    cardSize: CardSize,
    visible: Boolean,
    isWinner: Boolean,
    winnerHand: String?,
    indicatorPosition: IndicatorPosition
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isWinner) {
        if (isWinner) {
            scale.animateTo(
                targetValue = 1.15f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Column(
        modifier = modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Arrange indicator and cards based on position
        if (indicatorPosition == IndicatorPosition.BOTTOM) {
            if (isWinner && winnerHand != null) {
                WinnerHandLabel(winnerHand)
                Spacer(modifier = Modifier.height(Dimens.grid_0_5))
            }
            // Cards first, then indicator (for top positions)
            CardsRow(cards, cardSize, visible)

            Spacer(modifier = Modifier.height(Dimens.grid_1))

            PlayerIndicator(position, isWinner)
        } else {
            // Indicator first, then cards (for bottom positions)
            PlayerIndicator(position, isWinner)

            Spacer(modifier = Modifier.height(Dimens.grid_1))

            CardsRow(cards, cardSize, visible)

            if (isWinner && winnerHand != null) {
                Spacer(modifier = Modifier.height(Dimens.grid_0_5))
                WinnerHandLabel(winnerHand)
            }
        }
    }
}

@Composable
private fun PlayerIndicator(position: String, isWinner: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isWinner) Color(0xFFFFD700) else Color(0xFF37474F),
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = if (isWinner) Color(0xFFFFAB00) else Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = position,
            color = if (isWinner) Color.Black else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CardsRow(cards: List<Card>, cardSize: CardSize, visible: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.grid_0_5)
    ) {
        cards.forEachIndexed { index, card ->
            AnimatedPlayerCard(
                card = card,
                cardSize = cardSize,
                visible = visible,
                delay = index * 100L
            )
        }
    }
}

@Composable
private fun WinnerHandLabel(winnerHand: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFFFD700),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = Dimens.grid_1, vertical = Dimens.grid_0_5)
    ) {
        Text(
            text = winnerHand,
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AnimatedPlayerCard(
    card: Card,
    cardSize: CardSize,
    visible: Boolean,
    delay: Long
) {
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay(delay)
            show = true
        } else {
            show = false
        }
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(
            animationSpec = tween(300),
            initialScale = 0.3f
        ),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(animationSpec = tween(200))
    ) {
        PlayerCard(card, cardSize)
    }
}

@Composable
private fun AnimatedCommunityCard(
    card: Card,
    cardSize: CardSize,
    visible: Boolean,
    delay: Long
) {
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay(delay)
            show = true
        } else {
            show = false
        }
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(animationSpec = tween(400)) + scaleIn(
            animationSpec = tween(400),
            initialScale = 0.5f
        ),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = cardSize.shape
                )
        ) {
            PlayerCard(card, cardSize)
        }
    }
}
