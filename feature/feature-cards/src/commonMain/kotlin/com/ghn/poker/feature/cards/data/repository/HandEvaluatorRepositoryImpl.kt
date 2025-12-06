package com.ghn.poker.feature.cards.data.repository

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.cards.data.sources.remote.EvaluatorRemoteDataSource
import com.ghn.poker.feature.cards.domain.repository.HandEvaluatorRepository
import org.koin.core.annotation.Single

@Single([HandEvaluatorRepository::class])
class HandEvaluatorRepositoryImpl(
    private val evaluatorRemoteDataSource: EvaluatorRemoteDataSource
) : HandEvaluatorRepository {

    override suspend fun evaluate(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int
    ): ApiResponse<EvaluatorResponse, Exception> {
        return evaluatorRemoteDataSource.evaluate(
            heroCards = heroCards,
            boardCardsFiltered = boardCardsFiltered,
            villainCards = villainCards,
            simulationCount = simulationCount
        )
    }

    override suspend fun getFiveCardRank(heroCards: List<Card>): ApiResponse<Short, Exception> {
        return evaluatorRemoteDataSource.getFiveCardRank(heroCards)
    }
}
