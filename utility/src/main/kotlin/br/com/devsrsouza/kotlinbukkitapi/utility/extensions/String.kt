package br.com.devsrsouza.kotlinbukkitapi.utility.extensions

private val unicodeRegex = "((\\\\u)([0-9]{4}))".toRegex()

public fun String.javaUnicodeToCharacter(): String = unicodeRegex.replace(this) {
    String(charArrayOf(it.destructured.component3().toInt(16).toChar()))
}

public fun <T> T.print(): T = also { println(it) }

public fun String.centralize(
        length: Int,
        spacer: String = " ",
        prefix: String = "",
        suffix: String = ""
): String {
    if (this.length >= length) return this
    val part = prefix + spacer.repeat((length - this.length) / 2) + suffix
    return part + this + part
}

public val TRUE_CASES: Array<String> = arrayOf("true")
    get() = field.clone()
public val FALSE_CASES: Array<String> = arrayOf("false")
    get() = field.clone()

public fun String.toBooleanOrNull(
    trueCases: Array<String> = TRUE_CASES,
    falseCases: Array<String> = FALSE_CASES
): Boolean? = when {
    trueCases.any { it.equals(this, true) } -> true
    falseCases.any { it.equals(this, true) } -> false
    else -> null
}