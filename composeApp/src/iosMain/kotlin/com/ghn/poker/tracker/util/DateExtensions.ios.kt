package com.ghn.poker.tracker.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter

internal actual fun LocalDateTime.format(format: String): String? {
    val date = NSCalendar
        .currentCalendar
        .dateFromComponents(toNSDateComponents())
    return date?.let {
        val returnedDateFormat = NSDateFormatter()
        returnedDateFormat.dateFormat = format
        returnedDateFormat.stringFromDate(it)
    }
}
