package com.ghn.poker.tracker.domain.repository

import kotlinx.datetime.LocalDateTime
import session.Session

interface SessionRepository {
    suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?,
    )

    suspend fun getSessions(): List<Session>
}
