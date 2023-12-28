package com.ghn.poker.tracker.domain.usecase.impl

import com.ghn.gizmodb.common.models.GameType
import com.ghn.poker.tracker.data.repository.Session
import com.ghn.poker.tracker.data.sources.remote.ApiResponse
import com.ghn.poker.tracker.domain.repository.SessionRepository
import com.ghn.poker.tracker.domain.usecase.SessionData
import com.ghn.poker.tracker.domain.usecase.SessionUseCase
import kotlinx.datetime.Instant
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

    override suspend fun createSession(
        date: Instant,
        startAmount: Double?,
        endAmount: Double?,
        gameType: GameType,
    ): ApiResponse<Unit, Exception> {
        return sessionRepository.createSession(date, startAmount, endAmount, gameType)
    }

    override suspend fun getSessions(): ApiResponse<List<SessionData>, Exception> {
        return when (val sessions = sessionRepository.getSessions()) {
            is ApiResponse.Error -> sessions
            is ApiResponse.Success -> ApiResponse.Success(
                sessions.body.toSessionData()
            )
        }
    }
}

private fun List<Session>.toSessionData(): List<SessionData> =
    map {
        SessionData(
            date = it.date,
            startAmount = it.startAmount,
            endAmount = it.endAmount
        )
    }
