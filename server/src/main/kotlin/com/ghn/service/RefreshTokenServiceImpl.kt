package com.ghn.service

import com.ghn.model.TokenResponse
import com.ghn.plugins.JwtConfig
import com.ghn.repository.ApiCallResult
import com.ghn.repository.RefreshTokenRepository

class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenService {

    override fun refresh(refreshToken: String): ApiCallResult<TokenResponse> {
        return when (val result = refreshTokenRepository.validateAndGetUser(refreshToken)) {
            is ApiCallResult.Success -> {
                val userDTO = result.data

                // Generate new tokens
                val newAccessToken = JwtConfig.makeToken(userDTO)
                val newRefreshToken = refreshTokenRepository.generateRefreshToken(userDTO.id)

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
}
