package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.repository.HandEvaluatorRepository
import com.ghn.poker.tracker.domain.usecase.EquityCalculationUseCase
import org.koin.core.annotation.Factory

@Factory([EquityCalculationUseCase::class])
internal class EquityCalculationUseCaseImpl(
    private val evaluatorRepository: HandEvaluatorRepository
) : EquityCalculationUseCase {

    override suspend fun getResults(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
    ): ApiResponse<EvaluatorResponse, Exception> {
        return evaluatorRepository.evaluate(heroCards, boardCardsFiltered, villainCards)
    }

    companion object {
        const val SIMULATION_COUNT = 20000.0
    }
}
