package com.ghn.poker.tracker.domain.usecase

import com.ghn.gizmodb.common.models.Card
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.repository.HandEvaluatorRepository
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