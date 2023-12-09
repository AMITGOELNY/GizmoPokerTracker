package com.ghn.poker.tracker.domain.repository

import com.ghn.poker.tracker.data.repository.Session
import kotlinx.datetime.LocalDateTime

interface SessionRepository {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?,
    )

    suspend fun getSessions(): List<Session>
}
