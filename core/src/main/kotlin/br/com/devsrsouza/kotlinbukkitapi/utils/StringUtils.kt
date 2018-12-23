package br.com.devsrsouza.kotlinbukkitapi.utils

private val unicodeRegex = "((\\\\u)([0-9]{4}))".toRegex()

fun String.javaUnicodeToCharacter() = unicodeRegex.replace(this) {
    String(charArrayOf(it.destructured.component3().toInt(16).toChar()))
}

fun <T> T.print() = also { println(it) }

fun String.centralize(length: Int, spacer: String = " ", prefix: String = "", suffix: String = ""): String {
    if (this.length >= length) return this
    val part = prefix + spacer.repeat((length - this.length) / 2) + suffix
    return part + this + part
}

val TRUE_CASES = arrayOf("true")
    get() = field.clone()
val FALSE_CASES = arrayOf("false")
    get() = field.clone()

fun String.toBooleanOrNull(trueCases: Array<String> = TRUE_CASES,
                           falseCases: Array<String> = FALSE_CASES): Boolean? {
    return when {
        trueCases.any { it.equals(this, true) } -> true
        falseCases.any { it.equals(this, true) } -> false
        else -> null
    }
}