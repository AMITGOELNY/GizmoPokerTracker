package com.ghn.poker.feature.tracker.domain.repository

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.core.network.ApiResponse
import com.ghn.poker.feature.tracker.domain.model.Session
import kotlinx.datetime.LocalDateTime

interface SessionRepository {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?,
    )

    suspend fun createSession(
        date: kotlin.time.Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception>

    suspend fun getSessions(): ApiResponse<List<Session>, Exception>
}
