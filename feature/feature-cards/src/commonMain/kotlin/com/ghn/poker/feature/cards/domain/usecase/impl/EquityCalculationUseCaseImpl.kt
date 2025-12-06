package com.ghn.poker.feature.cards.domain.usecase.impl

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.repository.HandEvaluatorRepository
import com.ghn.poker.feature.cards.domain.usecase.EquityCalculationUseCase
import org.koin.core.annotation.Factory

@Factory([EquityCalculationUseCase::class])
internal class EquityCalculationUseCaseImpl(
    private val evaluatorRepository: HandEvaluatorRepository
) : EquityCalculationUseCase {

    override suspend fun getResults(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int
    ): ApiResponse<EvaluatorResponse, Exception> {
        return evaluatorRepository.evaluate(
            heroCards = heroCards,
            boardCardsFiltered = boardCardsFiltered,
            villainCards = villainCards,
            simulationCount = simulationCount
        )
    }
}
