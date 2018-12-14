package br.com.devsrsouza.kotlinbukkitapi.utils

inline fun <T> whenErrorNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Throwable) {
        null
    }
}

inline fun <T> whenErrorDefault(default: T, block: () -> T): T {
    return try {
        block()
    } catch (e: Throwable) {
        default
    }
}