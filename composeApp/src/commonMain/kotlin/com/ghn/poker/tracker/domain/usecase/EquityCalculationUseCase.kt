package com.ghn.poker.tracker.domain.usecase

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.tracker.data.sources.remote.ApiResponse

interface EquityCalculationUseCase {
    suspend fun getResults(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>
    ): ApiResponse<EvaluatorResponse, Exception>
}
