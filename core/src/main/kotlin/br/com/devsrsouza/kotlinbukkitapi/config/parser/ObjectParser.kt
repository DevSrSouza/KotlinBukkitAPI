package br.com.devsrsouza.kotlinbukkitapi.config.parser

enum class ParserType { STRING, MAP }

interface ObjectParser<T> {
    fun parse(any: Any): T

    fun render(element: T): Any
}
