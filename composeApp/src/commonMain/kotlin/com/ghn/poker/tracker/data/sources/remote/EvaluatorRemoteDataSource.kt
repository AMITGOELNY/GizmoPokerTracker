package com.ghn.poker.tracker.data.sources.remote

import com.ghn.gizmodb.common.models.Card
import com.ghn.gizmodb.common.models.EvaluatorRequest
import com.ghn.gizmodb.common.models.EvaluatorResponse
import com.ghn.poker.tracker.data.api.GizmoApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Single

interface EvaluatorRemoteDataSource {
    suspend fun evaluate(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int
    ): ApiResponse<EvaluatorResponse, Exception>

    suspend fun getFiveCardRank(heroCards: List<Card>): ApiResponse<Short, Exception>
}

@Single([EvaluatorRemoteDataSource::class])
internal class EvaluatorRemoteDataSourceImpl(
    private val apiClient: GizmoApiClient
) : EvaluatorRemoteDataSource {
    override suspend fun evaluate(
        heroCards: List<Card>,
        boardCardsFiltered: List<Card>,
        villainCards: List<Card>,
        simulationCount: Int
    ): ApiResponse<EvaluatorResponse, Exception> {
        return try {
            return apiClient.http.safeRequest {
                post {
                    url { path("evaluator") }
                    setBody(
                        EvaluatorRequest(
                            heroCards = heroCards,
                            villainCards = villainCards,
                            boardCardsFiltered = boardCardsFiltered,
                            simulationCount = simulationCount
                        )
                    )
                }
                    .body<EvaluatorResponse>()
            }
        } catch (e: Exception) {
            ApiResponse.Error.NetworkError
        }
    }

    override suspend fun getFiveCardRank(heroCards: List<Card>): ApiResponse<Short, Exception> {
        return try {
            return apiClient.http.safeRequest {
                post {
                    url { path("evaluator/hand-rank") }
                    setBody(heroCards)
                }
                    .body<Short>()
            }
        } catch (e: Exception) {
            ApiResponse.Error.NetworkError
        }
    }
}
