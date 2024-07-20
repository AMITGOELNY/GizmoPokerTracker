package com.ghn.gizmodb.common.models

import kotlinx.serialization.Serializable

@Serializable
data class EvaluatorResponse(
    val heroResult: Int,
    val villainResult: Int,
    val tiedResult: Int,
)
