package com.ghn.service

import com.ghn.model.TokenResponse
import com.ghn.model.User
import com.ghn.repository.ApiCallResult

interface UserService {
    fun login(username: String, password: String): ApiCallResult<TokenResponse>
    fun create(user: User): ApiCallResult<Unit>
}
