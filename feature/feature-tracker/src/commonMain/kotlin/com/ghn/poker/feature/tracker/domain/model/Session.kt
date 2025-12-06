package com.ghn.poker.feature.tracker.domain.model

import com.ghn.gizmodb.common.models.Venue

data class Session(
    val id: String,
    val date: kotlin.time.Instant,
    val startAmount: String?,
    val endAmount: String?,
    val venue: Venue?,
)
