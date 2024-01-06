package com.ghn.poker.tracker.domain.model

import kotlinx.datetime.LocalDate

data class FeedItem(
    val link: String,
    val image: String,
    val title: String,
    val pubDate: LocalDate,
    val site: String,
)
