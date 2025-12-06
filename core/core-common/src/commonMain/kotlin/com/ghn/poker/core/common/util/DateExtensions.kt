package com.ghn.poker.core.common.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

const val DAY_AND_MONTH_FORMAT = "MMM dd"
const val DAY_MONTH_AND_YEAR_FORMAT = "MMMM dd, yyyy h:mm a"

expect fun LocalDateTime.format(format: String): String?

expect fun LocalDate.format(format: String): String?
