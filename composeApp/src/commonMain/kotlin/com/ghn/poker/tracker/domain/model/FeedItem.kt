package com.ghn.poker.tracker.domain.model

import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.poker.tracker.util.format
import kotlinx.datetime.LocalDate

data class FeedItem(
    val link: String,
    val image: String,
    val title: String,
    val pubDate: LocalDate,
    val category: NewsCategory,
    val site: String,
) {
    val dateFormatted: String get() = pubDate.format("").orEmpty()
}
