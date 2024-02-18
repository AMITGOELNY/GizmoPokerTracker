package com.ghn.gizmodb.evaluator.models

private object JvmResourceReader

actual suspend fun readFile(path: String): ByteArray {
    val classLoader = Thread.currentThread().contextClassLoader ?: JvmResourceReader.javaClass.classLoader
    val resource = classLoader.getResourceAsStream(path) ?: throw MissingResourceException(path)
    return resource.readBytes()
}