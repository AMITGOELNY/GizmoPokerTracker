package com.ghn.poker.tracker.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
