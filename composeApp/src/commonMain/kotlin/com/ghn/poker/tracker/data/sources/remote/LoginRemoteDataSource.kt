package com.ghn.poker.tracker.data.sources.remote

interface LoginRemoteDataSource {
    suspend fun login(username: String, password: String): ApiResponse<String, Exception>

    suspend fun create(username: String, password: String): ApiResponse<String, Exception>
}
