package com.ghn.poker.core.common.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
