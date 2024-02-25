package com.ghn.poker.tracker.domain.usecase

import com.ghn.poker.tracker.domain.model.Card
import com.ghn.poker.tracker.presentation.cards.CardState

interface EquityCalculationUseCase {
    suspend fun evaluateCards(
        heroCards: List<Card?>,
        villainCards: List<Card?>,
        boardCards: List<Card?>,
        deck: List<CardState>,
    ): IntArray
}
