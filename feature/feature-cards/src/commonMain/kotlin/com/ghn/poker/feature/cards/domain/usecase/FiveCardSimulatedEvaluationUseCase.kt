package com.ghn.poker.feature.cards.domain.usecase

import com.ghn.gizmodb.common.models.Card
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.domain.repository.HandEvaluatorRepository
import org.koin.core.annotation.Factory

@Factory([FiveCardSimulatedEvaluationUseCase::class])
class FiveCardSimulatedEvaluationUseCase(
    private val evaluatorRepository: HandEvaluatorRepository
) {
    suspend fun getFiveCardRank(
        heroCards: List<Card>
    ): ApiResponse<Short, Exception> {
        return evaluatorRepository.getFiveCardRank(heroCards)
    }
}
