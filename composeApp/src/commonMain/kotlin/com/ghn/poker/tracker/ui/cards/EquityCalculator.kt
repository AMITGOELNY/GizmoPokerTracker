package com.ghn.poker.tracker.ui.cards

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.CardSuit
import com.ghn.poker.tracker.presentation.cards.CardRowType
import com.ghn.poker.tracker.presentation.cards.EquityCalculatorActions
import com.ghn.poker.tracker.presentation.cards.EquityCalculatorViewModel
import com.ghn.poker.tracker.presentation.cards.HandResults
import com.ghn.poker.tracker.presentation.cards.model.CardSize
import com.ghn.poker.tracker.presentation.cards.model.drawable
import com.ghn.poker.tracker.ui.shared.PrimaryButton
import com.ghn.poker.tracker.ui.theme.Dimens
import gizmopoker.composeapp.generated.resources.Res
import gizmopoker.composeapp.generated.resources.percent
import gizmopoker.composeapp.generated.resources.pick_a_suit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun EquityCalculator(
    viewModel: EquityCalculatorViewModel = koinInject()
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState().value

    Column(Modifier.fillMaxSize().padding(Dimens.grid_2_5)) {
        Text(
            text = "Equity Calculator",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        )
        Spacer(Modifier.height(Dimens.grid_2))
        CardRow(CardRowType.BOARD, state.boardCards) { index, type ->
            viewModel.dispatch(EquityCalculatorActions.BottomSheetUpdate(true, type, index))
        }
        Spacer(Modifier.height(Dimens.grid_2))
        CardRow(CardRowType.HERO, state.heroCard, state.results) { index, type ->
            viewModel.dispatch(EquityCalculatorActions.BottomSheetUpdate(true, type, index))
        }
        Spacer(Modifier.height(Dimens.grid_2))
        CardRow(CardRowType.VILLAIN, state.villainCard, state.results) { index, type ->
            viewModel.dispatch(EquityCalculatorActions.BottomSheetUpdate(true, type, index))
        }
        Spacer(Modifier.weight(1f))
        AnimatedVisibility(state.calculateEnabled) {
            PrimaryButton(
                buttonText = "Calculate",
                onClick = { viewModel.dispatch(EquityCalculatorActions.CalculateEquity) },
                isEnabled = !state.isCalculating,
                showLoading = state.isCalculating
            )
        }
    }

    val itemsList by remember { mutableStateOf(CardSuit.entries) }

    if (state.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.dispatch(EquityCalculatorActions.BottomSheetClose)
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.grid_3),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(Res.string.pick_a_suit))
                Spacer(Modifier.height(Dimens.grid_1))
                LazyRow {
                    items(itemsList) { item ->
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 6.dp)
                                .height(70.dp)
                                .aspectRatio(.75f),
                            selected = (item == state.selectedSuit),
                            onClick = {
                                viewModel.dispatch(EquityCalculatorActions.UpdateSuit(item))
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
                                        modifier = Modifier.size(Dimens.grid_2)
                                    )
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                selectedContainerColor = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = .5f
                                ),
                            ),
                            border = filterChipBorder(
                                enabled = true,
                                selected = (item == state.selectedSuit),
                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                                selectedBorderWidth = 2.dp
                            )
                        )
                    }
                }

                Spacer(Modifier.height(Dimens.grid_3))

                LazyRow(
                    modifier = Modifier.padding(horizontal = Dimens.grid_3),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.grid_1)
                ) {
                    items(state.sheetDisplayCards) {
                        PlayerCard(
                            it.card,
                            CardSize.EXTRA_SMALL,
                            disabled = it.disabled,
                            selected = it.card == state.selectorInfo?.selectedCard
                        ) {
                            viewModel.dispatch(EquityCalculatorActions.OnCardSelected(it.card))
                        }
                    }
                }
                Spacer(Modifier.height(Dimens.grid_3))
                PrimaryButton(
                    buttonText = "Confirm",
                    onClick = {
                        viewModel.dispatch(EquityCalculatorActions.OnConfirmSelected)
                    },
                    isEnabled = state.selectorInfo?.selectedCard != null,
                    modifier = Modifier.padding(Dimens.grid_3)
                )
                Spacer(Modifier.height(Dimens.grid_3))
            }
        }
    }
}

@Composable
internal fun CardRow(
    board: CardRowType,
    cardsCat: List<Card?>,
    results: HandResults? = null,
    onClick: (Int, CardRowType) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = board.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(.2f)
        )

        Box(modifier = Modifier.weight(.8f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            this@Row.AnimatedVisibility(
                visible = results != null,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                if (board == CardRowType.HERO) {
                    Text("${results?.winPercent}${stringResource(Res.string.percent)}")
                } else if (board == CardRowType.VILLAIN) {
                    Text("${results?.lossPercent}${stringResource(Res.string.percent)}")
                }
            }
        }
    }
}
