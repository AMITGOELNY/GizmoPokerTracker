package com.ghn.repository

import com.ghn.gizmodb.common.models.UserDTO

interface RefreshTokenRepository {
    fun generateRefreshToken(userId: Int): String
    fun validateAndGetUser(token: String): ApiCallResult<UserDTO>
    fun revokeToken(token: String): ApiCallResult<Unit>
    fun revokeAllUserTokens(userId: Int): ApiCallResult<Unit>
}
