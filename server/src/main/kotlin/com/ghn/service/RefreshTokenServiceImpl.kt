package com.ghn.service

import com.ghn.model.TokenResponse
import com.ghn.plugins.JwtConfig
import com.ghn.repository.ApiCallResult
import com.ghn.repository.RefreshTokenRepository
import org.koin.core.annotation.Single

@Single([RefreshTokenService::class])
class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenService {

    override fun refresh(refreshToken: String): ApiCallResult<TokenResponse> {
        return when (val result = refreshTokenRepository.validateAndGetUser(refreshToken)) {
            is ApiCallResult.Success -> {
                val userDTO = result.data
                val userId = userDTO.id ?: return ApiCallResult.Failure("User ID not found")

                // Generate new tokens
                val newAccessToken = JwtConfig.makeToken(userDTO)
                val newRefreshToken = refreshTokenRepository.generateRefreshToken(userId)

                // Revoke old refresh token (rotation)
                refreshTokenRepository.revokeToken(refreshToken)

                ApiCallResult.Success(TokenResponse(newAccessToken, newRefreshToken))
            }
            ApiCallResult.BadPassword,
            ApiCallResult.NotFound,
            ApiCallResult.Unauthorized -> ApiCallResult.Unauthorized

            is ApiCallResult.Failure -> result
        }
    }

    override fun logout(userId: Int): ApiCallResult<Unit> {
        return refreshTokenRepository.revokeAllUserTokens(userId)
    }
}
