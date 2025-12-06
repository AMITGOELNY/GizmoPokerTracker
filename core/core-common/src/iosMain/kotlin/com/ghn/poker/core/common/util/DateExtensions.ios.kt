package com.ghn.poker.core.common.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

private const val ISO_8601_FORMAT = "yyyy-MM-dd"

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

internal actual fun LocalDate.format(format: String): String? {
    val dateFormatter = NSDateFormatter().apply {
        dateFormat = ISO_8601_FORMAT
    }

    val formatStyle: NSDateFormatterStyle = NSDateFormatterMediumStyle
    val timeFormatStyle = NSDateFormatterNoStyle

    val date: NSDate? = dateFormatter.dateFromString(
        string = toString()
    )
    return date?.let {
        val returnedDateFormat = NSDateFormatter().apply {
            dateStyle = formatStyle
            timeStyle = timeFormatStyle
            locale = NSLocale.currentLocale
        }
        returnedDateFormat.stringFromDate(it).replace(" ", " ")
    }
}
