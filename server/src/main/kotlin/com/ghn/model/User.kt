package com.ghn.model

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String
) : Principal
