package com.ghn.service

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.repository.SessionRepository
import com.ghn.repository.SessionResponse
import io.ktor.http.HttpStatusCode
import org.koin.core.annotation.Single

@Single([SessionService::class])
class SessionServiceImpl(
    private val repository: SessionRepository
) : SessionService {
    override fun getUserSessions(id: Int): List<SessionDTO> {
        return repository.getUserSessions(id)
    }

    override fun getUserSessionById(sessionId: String, userId: Int): SessionResponse<SessionDTO> {
        return repository.getUserSessionById(sessionId, userId)
    }

    override fun createSession(userSession: SessionDTO, id: Int) {
        repository.createSession(userSession, id)
    }

    override fun deleteSession(sessionId: String, userId: Int): HttpStatusCode {
        return repository.deleteSession(sessionId, userId)
    }
}
