package com.ghn.poker.tracker.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual class DecimalFormat actual constructor() {
    actual fun format(double: Double): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 1u
        formatter.maximumFractionDigits = 1u
        formatter.numberStyle = 1u //Decimal
        return formatter.stringFromNumber(NSNumber(double))!!
    }
}
