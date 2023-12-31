package com.ghn.poker.tracker.domain.repository

import com.ghn.gizmodb.common.models.GameType
import com.ghn.gizmodb.common.models.Venue
import com.ghn.poker.tracker.data.repository.Session
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface SessionRepository {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?,
    )

    suspend fun createSession(
        date: Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
        venue: Venue,
    ): ApiResponse<Unit, Exception>

    suspend fun getSessions(): ApiResponse<List<Session>, Exception>
}
