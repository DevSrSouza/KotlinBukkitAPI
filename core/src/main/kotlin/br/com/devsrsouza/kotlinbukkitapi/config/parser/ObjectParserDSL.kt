package br.com.devsrsouza.kotlinbukkitapi.config.parser

import kotlin.reflect.KType

inline fun <T> newParser(builder: ObjectParserDSLBuilder<T>.() -> Unit): ObjectParser<T> {
    return ObjectParserDSLImpl<T>().apply(builder)
}

typealias ParseCallback<T> = (any: Any) -> T
typealias RenderCallback<T> = (element: T) -> Pair<Any, KType>

interface ObjectParserDSLBuilder<T> {
    fun parse(callback: ParseCallback<T>)

    fun render(callback: RenderCallback<T>)
}

class ObjectParserDSLImpl<T> : ObjectParser<T>, ObjectParserDSLBuilder<T> {

    private lateinit var parseCallback: ParseCallback<T>
    private lateinit var renderCallback: RenderCallback<T>

    override fun parse(any: Any): T {
        return parseCallback(any)
    }

    override fun render(element: T): Pair<Any, KType> {
        return renderCallback(element)
    }

    override fun parse(callback: ParseCallback<T>) {
        parseCallback = callback
    }

    override fun render(callback: RenderCallback<T>) {
        renderCallback = callback
    }
}