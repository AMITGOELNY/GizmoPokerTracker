package com.ghn.poker.tracker.data.repository

import com.ghn.poker.tracker.data.database.SessionDao
import com.ghn.poker.tracker.domain.repository.SessionRepository
import com.ghn.poker.tracker.util.randomUUID
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Single
import session.Session

@Single([SessionRepository::class])
class SessionRepositoryImpl(
    private val dao: SessionDao
) : SessionRepository {
    override suspend fun insertSession(
        date: LocalDateTime,
        startAmount: Double?,
        endAmount: Double?
    ) {
        dao.insertSession(
            Session(
                id = randomUUID(),
                date = date.toString(),
                startAmount = startAmount?.toString(),
                endAmount = endAmount?.toString()
            )
        )
    }

    override suspend fun getSessions(): List<Session> {
        return dao.getSessions()
    }
}
