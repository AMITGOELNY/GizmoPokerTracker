package com.ghn.repository

import com.ghn.gizmodb.common.models.SessionDTO
import com.ghn.gizmodb.tables.references.SESSION
import io.ktor.http.HttpStatusCode
import org.jooq.DSLContext

class SessionRepositoryImpl(private val db: DSLContext) : SessionRepository {
    override fun getUserSessions(id: Int): List<SessionDTO> {
        return db.fetch(SESSION, SESSION.USERID.eq(id)).into(SessionDTO::class.java)
    }

    override fun getUserSessionById(
        sessionId: String,
        userId: Int
    ): SessionResponse<SessionDTO> {
        val session = db.fetchOne(SESSION, SESSION.ID.eq(sessionId), SESSION.USERID.eq(userId))
            ?.into(SessionDTO::class.java)
        return if (session != null) {
            SessionResponse.Success(session)
        } else {
            SessionResponse.Error.HttpError(HttpStatusCode.BadRequest)
        }
    }

    override fun createSession(userSession: SessionDTO, id: Int) {
        val newRecord = db.newRecord(SESSION)
        newRecord.from(userSession)
        newRecord.userid = id
        newRecord.store()
    }

    override fun deleteSession(sessionId: String, userId: Int): HttpStatusCode {
        return try {
            val success = db.deleteFrom(SESSION)
                .where(SESSION.ID.eq(sessionId), SESSION.USERID.eq(userId))
                .execute()
            if (success != 0) {
                HttpStatusCode.OK
            } else {
                HttpStatusCode.NotAcceptable
            }
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}

sealed interface SessionResponse<out T> {
    /** Represents successful network responses (2xx). */
    data class Success<T>(val body: T) : SessionResponse<T>

    sealed interface Error : SessionResponse<Nothing> {
        /** Represents server (50x) and client (40x) errors. */
        data class HttpError(val code: HttpStatusCode) : Error

        /** Represent IOExceptions and connectivity issues. */
        data object NetworkError : Error

        /** Represent SerializationExceptions. */
        data object SerializationError : Error
    }
}
