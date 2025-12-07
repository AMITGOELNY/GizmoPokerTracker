package com.ghn.poker.feature.cards.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilterChipDefaults.filterChipBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.poker.core.ui.components.PrimaryButton
import com.ghn.poker.core.ui.preview.SurfacePreview
import com.ghn.poker.core.ui.theme.Dimens
import com.ghn.poker.feature.cards.presentation.CardRowType
import com.ghn.poker.feature.cards.presentation.EquityCalculatorAction
import com.ghn.poker.feature.cards.presentation.EquityCalculatorViewModel
import com.ghn.poker.feature.cards.presentation.HandResults
import com.ghn.poker.feature.cards.presentation.model.CardSize
import com.ghn.poker.feature.cards.presentation.model.CardState
import com.ghn.poker.feature.cards.presentation.model.drawable
import com.ghn.poker.feature.cards.ui.components.EquityResultsDisplayCombined
import gizmopoker.feature.feature_cards.generated.resources.Res
import gizmopoker.feature.feature_cards.generated.resources.calculate
import gizmopoker.feature.feature_cards.generated.resources.confirm
import gizmopoker.feature.feature_cards.generated.resources.pick_a_suit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun EquityCalculator(
    viewModel: EquityCalculatorViewModel = koinInject()
) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()

    EquityCalculatorContent(
        boardCards = state.boardCards,
        heroCard = state.heroCard,
        villainCard = state.villainCard,
        results = state.results,
        calculateEnabled = state.calculateEnabled,
        isCalculating = state.isCalculating,
        showBottomSheet = state.showBottomSheet,
        selectedSuit = state.selectedSuit,
        sheetDisplayCards = state.sheetDisplayCards,
        selectedCard = state.selectorInfo?.selectedCard,
        sheetState = sheetState,
        onDispatch = { action -> viewModel.onDispatch(action) }
    )
}

@Composable
private fun EquityCalculatorContent(
    boardCards: List<Card?>,
    heroCard: List<Card?>,
    villainCard: List<Card?>,
    results: HandResults?,
    calculateEnabled: Boolean,
    isCalculating: Boolean,
    showBottomSheet: Boolean,
    selectedSuit: CardSuit?,
    sheetDisplayCards: List<CardState>,
    selectedCard: Card?,
    sheetState: androidx.compose.material3.SheetState,
    onDispatch: (EquityCalculatorAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E1A),
                        Color(0xFF131629),
                        Color(0xFF1A1F35)
                    )
                )
            )
    ) {
        // Animated background elements
        AnimatedBackgroundElements()

        Column(
            Modifier
                .fillMaxSize()
                .padding(Dimens.grid_2)
        ) {
            Spacer(Modifier.height(Dimens.grid_1))

            // Card Rows in Cards
            CardRowCard(CardRowType.BOARD, boardCards) { index, type ->
                onDispatch(EquityCalculatorAction.BottomSheetUpdate(true, type, index))
            }
            Spacer(Modifier.height(Dimens.grid_1_5))
            CardRowCard(CardRowType.HERO, heroCard) { index, type ->
                onDispatch(EquityCalculatorAction.BottomSheetUpdate(true, type, index))
            }
            Spacer(Modifier.height(Dimens.grid_1_5))
            CardRowCard(CardRowType.VILLAIN, villainCard) { index, type ->
                onDispatch(EquityCalculatorAction.BottomSheetUpdate(true, type, index))
            }

            Spacer(Modifier.height(Dimens.grid_1_5))

            // Results Display
            AnimatedVisibility(visible = results != null) {
                results?.let { EquityResultsDisplayCombined(results = it) }
            }

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(calculateEnabled) {
                PrimaryButton(
                    buttonText = stringResource(Res.string.calculate),
                    onClick = { onDispatch(EquityCalculatorAction.CalculateEquity) },
                    isEnabled = !isCalculating,
                    showLoading = isCalculating
                )
            }
        }
    }

    val itemsList by remember { mutableStateOf(CardSuit.entries) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDispatch(EquityCalculatorAction.BottomSheetClose) },
            sheetState = sheetState,
            containerColor = Color(0xFF1F2438)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.grid_3),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.pick_a_suit),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(Dimens.grid_2))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1_5)) {
                    items(itemsList) { item ->
                        FilterChip(
                            modifier = Modifier
                                .height(70.dp)
                                .aspectRatio(.75f),
                            selected = (item == selectedSuit),
                            onClick = {
                                onDispatch(EquityCalculatorAction.UpdateSuit(item))
                            },
                            label = {
                                Box(
                                    Modifier.height(50.dp).aspectRatio(.65f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(item.drawable),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(Dimens.grid_3)
                                    )
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF2A3142),
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.2f
                                ),
                            ),
                            border = filterChipBorder(
                                enabled = true,
                                selected = (item == selectedSuit),
                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                                selectedBorderWidth = 2.dp,
                                borderColor = Color(0xFF3A4152)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(Dimens.grid_3))

                LazyRow(
                    modifier = Modifier.padding(horizontal = Dimens.grid_3),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1)
                ) {
                    items(sheetDisplayCards) {
                        PlayerCard(
                            it.card,
                            CardSize.EXTRA_SMALL,
                            disabled = it.disabled,
                            selected = it.card == selectedCard
                        ) {
                            onDispatch(EquityCalculatorAction.OnCardSelected(it.card))
                        }
                    }
                }
                Spacer(Modifier.height(Dimens.grid_3))
                PrimaryButton(
                    buttonText = stringResource(Res.string.confirm),
                    onClick = { onDispatch(EquityCalculatorAction.OnConfirmSelected) },
                    isEnabled = selectedCard != null,
                    modifier = Modifier.padding(horizontal = Dimens.grid_3)
                )
                Spacer(Modifier.height(Dimens.grid_3))
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundElements() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Subtle circular glow elements
        Box(
            modifier = Modifier
                .size(250.dp)
                .alpha(0.15f)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF5265FF),
                                Color.Transparent
                            )
                        ),
                        radius = size.width / 2
                    )
                }
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .alpha(0.1f)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF7B4FFF),
                                Color.Transparent
                            )
                        ),
                        radius = size.width / 2
                    )
                }
        )
    }
}

@Composable
private fun CardRowCard(
    board: CardRowType,
    cardsCat: List<Card?>,
    onClick: (Int, CardRowType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2438).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5265FF).copy(alpha = 0.3f),
                            Color(0xFF5265FF).copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(Dimens.grid_2)
        ) {
            CardRow(board, cardsCat, onClick)
        }
    }
}

@Composable
internal fun CardRow(
    board: CardRowType,
    cardsCat: List<Card?>,
    onClick: (Int, CardRowType) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = board.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(.2f)
        )

        Row(
            modifier = Modifier.weight(.8f).fillMaxWidth(),
            horizontalArrangement = if (board == CardRowType.BOARD) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.spacedBy(Dimens.grid_1_5, Alignment.End)
            }
        ) {
            (0 until board.count).forEach {
                val card = cardsCat.getOrNull(it)
                if (card == null) {
                    Box(
                        modifier = Modifier.width(CardSize.EXTRA_SMALL.width)
                            .aspectRatio(.66f)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable { onClick(it, board) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${it + 1}")
                    }
                } else {
                    PlayerCard(card, cardSize = CardSize.EXTRA_SMALL) {
                        onClick(it, board)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EquityCalculatorContentPreview() = SurfacePreview {
    val sheetState = rememberModalBottomSheetState()

    EquityCalculatorContent(
        boardCards = listOf(null, null, null, null, null),
        heroCard = listOf(null, null),
        villainCard = listOf(null, null),
        results = HandResults(
            winPercent = "65",
            lossPercent = "35",
            tiePercent = "0"
        ),
        calculateEnabled = true,
        isCalculating = false,
        showBottomSheet = false,
        selectedSuit = null,
        sheetDisplayCards = emptyList(),
        selectedCard = null,
        sheetState = sheetState,
        onDispatch = {}
    )
}

@Preview
@Composable
private fun CardRowCardPreview() = SurfacePreview {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E1A),
                        Color(0xFF131629)
                    )
                )
            )
            .padding(Dimens.grid_2_5)
    ) {
        CardRowCard(
            board = CardRowType.BOARD,
            cardsCat = listOf(null, null, null, null, null),
            onClick = { _, _ -> }
        )
    }
}
