package com.ghn.poker.tracker.domain.repository

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.tracker.data.sources.remote.ApiResponse

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
