package br.com.devsrsouza.kotlinbukkitapi.config.parser

import kotlin.reflect.KType

enum class ParserType { STRING, MAP }

interface ObjectParser<T> {
    fun parse(any: Any): T

    fun render(element: T): Pair<Any, KType>
}
