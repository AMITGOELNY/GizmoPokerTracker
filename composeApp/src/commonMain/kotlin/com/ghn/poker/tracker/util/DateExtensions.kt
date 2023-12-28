package com.ghn.poker.tracker.util

import kotlinx.datetime.LocalDateTime

const val DAY_AND_MONTH_FORMAT = "MMM dd"

// const val MONTH_AND_YEAR_FORMAT = "MMM yyyy"
const val DAY_MONTH_AND_YEAR_FORMAT = "MMMM dd, yyyy h:mm a"
// const val MONTH_FORMAT = "MMM"
// const val DAY_SHORT_FORMAT = "E"
// const val TIME_FORMAT = "h:mm a"

internal expect fun LocalDateTime.format(format: String): String?
