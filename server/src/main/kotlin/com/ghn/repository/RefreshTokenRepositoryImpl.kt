package com.ghn.repository

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.gizmodb.tables.references.REFRESH_TOKEN
import com.ghn.gizmodb.tables.references.USER
import org.jooq.DSLContext
import org.koin.core.annotation.Single
import java.security.MessageDigest
import java.util.UUID
import kotlin.time.Duration.Companion.days

@Single([RefreshTokenRepository::class])
class RefreshTokenRepositoryImpl(
    private val db: DSLContext
) : RefreshTokenRepository {

    companion object {
        private const val REFRESH_TOKEN_VALIDITY_DAYS = 30
    }

    override fun generateRefreshToken(userId: Int): String {
        val token = UUID.randomUUID().toString()
        val tokenHash = hashToken(token)
        val now = kotlin.time.Clock.System.now()
        val expiresAt = now + REFRESH_TOKEN_VALIDITY_DAYS.days

        val record = db.newRecord(REFRESH_TOKEN)
        record.id = UUID.randomUUID().toString()
        record.userId = userId
        record.tokenHash = tokenHash
        record.expiresAt = expiresAt
        record.createdAt = now
        record.store()

        return token
    }

    override fun validateAndGetUser(token: String): ApiCallResult<UserDTO> {
        val tokenHash = hashToken(token)
        val now = kotlin.time.Clock.System.now()

        // First check if token exists (even if expired) for theft detection
        val tokenRecord = db
            .select(REFRESH_TOKEN.USER_ID, REFRESH_TOKEN.EXPIRES_AT)
            .from(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.TOKEN_HASH.eq(tokenHash))
            .fetchOne()

        if (tokenRecord != null) {
            val userId = tokenRecord.get(REFRESH_TOKEN.USER_ID) ?: return ApiCallResult.NotFound
            val expiresAt = tokenRecord.get(REFRESH_TOKEN.EXPIRES_AT) ?: return ApiCallResult.NotFound

            // If token is expired, this might be a reuse attempt (token theft)
            // Revoke ALL tokens for this user as a security measure
            if (expiresAt <= now) {
                revokeAllUserTokens(userId)
                return ApiCallResult.NotFound
            }

            // Token is valid, get user info
            val user = db
                .select(USER.asterisk())
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOne()

            return if (user != null) {
                val userDTO = user.into(UserDTO::class.java)
                ApiCallResult.Success(userDTO)
            } else {
                ApiCallResult.NotFound
            }
        }

        return ApiCallResult.NotFound
    }

    override fun revokeToken(token: String): ApiCallResult<Unit> {
        val tokenHash = hashToken(token)
        val deleted = db
            .deleteFrom(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.TOKEN_HASH.eq(tokenHash))
            .execute()

        return if (deleted > 0) {
            ApiCallResult.Success(Unit)
        } else {
            ApiCallResult.NotFound
        }
    }

    override fun revokeAllUserTokens(userId: Int): ApiCallResult<Unit> {
        db
            .deleteFrom(REFRESH_TOKEN)
            .where(REFRESH_TOKEN.USER_ID.eq(userId))
            .execute()

        return ApiCallResult.Success(Unit)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
