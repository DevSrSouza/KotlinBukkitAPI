package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.PropertyLoadAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.PropertySaveAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.getDelegateFromType
import br.com.devsrsouza.kotlinbukkitapi.config.parser.ObjectParser
import br.com.devsrsouza.kotlinbukkitapi.config.parser.ObjectParserDSLBuilder
import br.com.devsrsouza.kotlinbukkitapi.config.parser.newParser
import kotlin.reflect.KProperty

fun <T> parsable(
        default: T,
        builder: ObjectParserDSLBuilder<T>.() -> Unit
) = ObjectParserDelegate(default, newParser(builder))

class ObjectParserDelegate<T>(val default: T, val parser: ObjectParser<T>) {
    private var value = default

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun parserParseAdapter(): PropertyLoadAdapter = builder@{ instance, property, value ->
    val delegate = property.getDelegateFromType<ObjectParserDelegate<Any>>(instance)
            ?: return@builder value

    delegate.parser.parse(value)
}

fun parserRenderAdapter(): PropertySaveAdapter = builder@{ instance, property, type, value ->
    val delegate = property.getDelegateFromType<ObjectParserDelegate<Any>>(instance)
            ?: return@builder value to type

    delegate.parser.render(value)
}