package com.ghn.poker.core.common.util

import java.text.DecimalFormat as JavaDecimalFormat

actual class DecimalFormat actual constructor() {
    actual fun format(double: Double): String {
        val decimalFormat = JavaDecimalFormat()
        decimalFormat.isGroupingUsed = false
        decimalFormat.maximumFractionDigits = 1
        decimalFormat.minimumFractionDigits = 1
        decimalFormat.isDecimalSeparatorAlwaysShown = true
        return decimalFormat.format(double)
    }
}
