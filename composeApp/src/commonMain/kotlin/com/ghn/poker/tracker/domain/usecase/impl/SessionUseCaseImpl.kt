package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.poker.tracker.data.repository.Session
import com.ghn.poker.tracker.domain.repository.SessionRepository
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.domain.usecase.SessionUseCase
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Factory

@Factory([SessionUseCase::class])
class SessionUseCaseImpl(
    private val sessionRepository: SessionRepository
) : SessionUseCase {
    override suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    ) {
        sessionRepository.insertSession(date, startAmount, endAmount)
    }

    override suspend fun getSessions(): List<SessionData> {
        val sessions = sessionRepository.getSessions()
        return sessions.toSessionData()
    }
}

private fun List<Session>.toSessionData(): List<SessionData> =
    map {
        SessionData(
            date = LocalDateTime.parse(it.date),
            startAmount = it.startAmount,
            endAmount = it.endAmount
        )
    }
