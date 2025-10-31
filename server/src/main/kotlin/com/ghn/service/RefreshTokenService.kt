package com.ghn.service

import com.ghn.model.TokenResponse
import com.ghn.repository.ApiCallResult

interface RefreshTokenService {
    fun refresh(refreshToken: String): ApiCallResult<TokenResponse>
}
