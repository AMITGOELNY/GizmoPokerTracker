package com.ghn.poker.feature.auth.domain.repository

import com.ghn.poker.core.network.ApiResponse

interface LoginRepository {
    suspend fun login(username: String, password: String): ApiResponse<Unit, Exception>
    suspend fun create(username: String, password: String): ApiResponse<Unit, Exception>
    suspend fun logout(): ApiResponse<Unit, Exception>
}
