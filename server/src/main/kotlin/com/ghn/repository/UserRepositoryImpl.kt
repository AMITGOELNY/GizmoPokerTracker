package com.ghn.repository

import com.ghn.gizmodb.common.models.UserDTO
import com.ghn.gizmodb.tables.references.USER
import com.ghn.mappers.toUserDTO
import com.ghn.model.TokenResponse
import com.ghn.model.User
import com.ghn.plugins.JwtConfig
import com.ghn.util.PasswordValidator
import org.jooq.DSLContext

class UserRepositoryImpl(
    private val db: DSLContext,
    private val refreshTokenRepository: RefreshTokenRepository
) : UserRepository {
    override fun login(username: String, password: String): ApiCallResult<TokenResponse> {
        val userDTO = db.fetchOne(USER, USER.USERNAME.eq(username))?.into(UserDTO::class.java)
        return if (userDTO != null) {
            val userId = userDTO.id ?: return ApiCallResult.Failure("User ID not found")
            val passwordValid = PasswordValidator.verifyPassword(password, userDTO.password)
            if (passwordValid) {
                val accessToken = JwtConfig.makeToken(userDTO)
                val refreshToken = refreshTokenRepository.generateRefreshToken(userId)
                val tokenResponse = TokenResponse(accessToken, refreshToken)
                ApiCallResult.Success(tokenResponse)
            } else {
                ApiCallResult.BadPassword
            }
        } else {
            ApiCallResult.NotFound
        }
    }

    override fun create(user: User): ApiCallResult<Unit> {
        val newRecord = db.newRecord(USER)
        newRecord.from(user.toUserDTO())
        newRecord.store()
        return ApiCallResult.Success(Unit)
    }
}

sealed interface ApiCallResult<out T> {
    data class Success<T>(val data: T) : ApiCallResult<T>
    data class Failure(val reason: String) : ApiCallResult<Nothing>
    data object BadPassword : ApiCallResult<Nothing>
    data object Unauthorized : ApiCallResult<Nothing>
    data object NotFound : ApiCallResult<Nothing>
}
