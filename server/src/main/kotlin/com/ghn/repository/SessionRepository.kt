package com.ghn.repository

import com.ghn.gizmodb.common.models.SessionDTO
import io.ktor.http.HttpStatusCode

interface SessionRepository {
    fun getUserSessions(id: Int): List<SessionDTO>
    fun getUserSessionById(sessionId: String, userId: Int): SessionResponse<SessionDTO>
    fun createSession(userSession: SessionDTO, id: Int)
    fun deleteSession(sessionId: String, userId: Int): HttpStatusCode
}
