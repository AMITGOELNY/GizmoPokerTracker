package com.ghn.poker.feature.tracker.domain.usecase

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.SessionData
import kotlinx.datetime.LocalDateTime

interface SessionUseCase {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    )

    suspend fun createSession(
        date: kotlin.time.Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception>

    suspend fun getSessions(): ApiResponse<List<SessionData>, Exception>
}
