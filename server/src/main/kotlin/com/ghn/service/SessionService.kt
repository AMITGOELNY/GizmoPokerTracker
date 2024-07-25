package com.ghn.service

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.repository.SessionResponse
import io.ktor.http.HttpStatusCode

interface SessionService {
    fun getUserSessions(id: Int): List<SessionDTO>
    fun getUserSessionById(sessionId: String, userId: Int): SessionResponse<SessionDTO>
    fun createSession(userSession: SessionDTO, id: Int)
    fun deleteSession(sessionId: String, userId: Int): HttpStatusCode
}
