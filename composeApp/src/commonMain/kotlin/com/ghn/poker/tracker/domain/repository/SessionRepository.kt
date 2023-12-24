package com.ghn.poker.tracker.domain.repository

import com.ghn.poker.tracker.data.repository.Session
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import kotlinx.datetime.LocalDateTime

interface SessionRepository {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?,
    )

    suspend fun getSessions(): ApiResponse<List<Session>, Exception>
}
