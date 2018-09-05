package br.com.devsrsouza.kotlinbukkitapi.utils

private val unicodeRegex = "((\\\\u)([0-9]{4}))".toRegex()

fun String.javaUnicodeToCharacter() = unicodeRegex.replace(this) {
    String(charArrayOf(it.destructured.component3().toInt(16).toChar()))
}

fun <T> T.print() = also { println(it) }
