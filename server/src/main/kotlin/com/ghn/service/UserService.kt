package com.ghn.service

import com.ghn.model.User
import com.ghn.repository.ApiCallResult

interface UserService {
    fun login(username: String, password: String): ApiCallResult<String>
    fun create(user: User): ApiCallResult<Unit>
}
