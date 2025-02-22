package com.ghn.poker.tracker.domain.usecase

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.tracker.data.sources.remote.ApiResponse

const val DEFAULT_SIMULATION_COUNT = 20000

interface EquityCalculationUseCase {
    suspend fun getResults(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int = DEFAULT_SIMULATION_COUNT
    ): ApiResponse<EvaluatorResponse, Exception>
}
