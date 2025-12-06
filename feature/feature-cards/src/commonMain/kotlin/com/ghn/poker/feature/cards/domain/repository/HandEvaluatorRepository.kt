package com.ghn.poker.feature.cards.domain.repository

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse

interface HandEvaluatorRepository {
    suspend fun evaluate(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int,
    ): ApiResponse<EvaluatorResponse, Exception>

    suspend fun getFiveCardRank(
        heroCards: List<Card>
    ): ApiResponse<Short, Exception>
}
