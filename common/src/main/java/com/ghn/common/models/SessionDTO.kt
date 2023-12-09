package com.ghn.common.models

import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    val id: String,
    val date: String,
    val startAmount: String?,
    val endAmount: String?,
)
