package com.ghn.poker.core.common.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

actual fun LocalDateTime.format(format: String): String? =
    DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())

actual fun LocalDate.format(format: String): String? {
    val datetime = LocalDateTime(this, LocalTime(0, 0, 0))

    return try {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            .withZone(ZoneId.from(ZoneOffset.UTC))
            .withLocale(Locale.getDefault())
        formatter.format(datetime.toJavaLocalDateTime())
    } catch (e: Exception) {
        e.toString()
    }
}
