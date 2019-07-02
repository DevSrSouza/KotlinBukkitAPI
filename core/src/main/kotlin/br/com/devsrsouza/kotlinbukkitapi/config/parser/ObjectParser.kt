package br.com.devsrsouza.kotlinbukkitapi.config.parser

interface ObjectParser<T> {
    fun parse(any: Any): T

    fun render(element: T): Any
}
