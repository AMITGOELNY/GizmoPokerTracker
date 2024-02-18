package com.ghn.gizmodb.evaluator.models

import gizmopoker.evaluator.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
actual suspend fun readFile(path: String): ByteArray {
    // TODO: Replace with native file io.  Remove Compose Resource support
    return Res.readBytes(path)
}
