package com.ghn.gizmodb.evaluator.models

/**
 * Contains all of the HashTables used for either no flush results or flush results.
 *
 * Due to the size of the tables they're loaded lazily on demand.
 */
internal object HashTable {
    private var FLUSH: ShortArray? = null

    private lateinit var noFlush5: ShortArray
    private lateinit var noFlush7: ShortArray

    suspend fun getNoFlush5(): ShortArray {
        if (!this::noFlush5.isInitialized) {
            noFlush5 = loadFromFile("hashtable5")
        }
        return noFlush5
    }

    suspend fun getNoFlush7(): ShortArray {
        if (!this::noFlush7.isInitialized) {
            noFlush7 = loadFromFile("hashtable7")
        }
        return noFlush7
    }

    private suspend fun loadFromFile(fileName: String): ShortArray {
        try {
            val bytes = readFile("hash.table/$fileName.txt")
            val s: String = bytes.decodeToString()
            println(s)
            val split = s.split("\n".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val amount = split[0].toInt()
            val arr = ShortArray(amount)
            val split2 = split[1].split(",".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in 0 until amount) {
                val f = split2[i]
                arr[i] = f.toShort()
            }
            return arr
        } catch (e: Exception) {
            println(e.message)
            throw RuntimeException(e) //TODO make this suck less
        }
    }

//    fun FLUSH(): ShortArray? {
//        if (FLUSH == null) {
//            FLUSH = loadFromFile("HashTable")
//        }
//        return FLUSH
//    }
}

suspend fun readFile(path: String): ByteArray {
    val classLoader = Thread.currentThread().contextClassLoader //?: javaClass.classLoader
    val resource = classLoader.getResourceAsStream(path) ?: throw MissingResourceException(path)
    return resource.readBytes()
}

class MissingResourceException(path: String) : Exception("Missing resource with path: $path")
