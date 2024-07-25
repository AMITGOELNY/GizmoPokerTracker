package com.ghn.repository

import com.ghn.model.User

interface UserRepository {
    fun login(username: String, password: String): ApiCallResult<String>
    fun create(user: User): ApiCallResult<Unit>
}
