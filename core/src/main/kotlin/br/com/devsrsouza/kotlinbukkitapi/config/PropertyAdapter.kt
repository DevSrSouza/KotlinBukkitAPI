package br.com.devsrsouza.kotlinbukkitapi.config

import br.com.devsrsouza.kotlinbukkitapi.config.adapter.chatColorLoadAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.chatColorSaveAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.parserParseAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.parserRenderAdapter
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType

typealias PropertySaveAdapter = (instance: Any, property: KMutableProperty1<Any, Any>, type: KType, value: Any) -> Pair<Any, KType>
typealias PropertyLoadAdapter = (instance: Any, property: KMutableProperty1<Any, Any>, value: Any) -> Any

fun emptySaveAdapter(): PropertySaveAdapter = { _, _, type, value -> value to type }
fun emptyLoadAdapter(): PropertyLoadAdapter = { _, _, value -> value }

fun defaultSaveAdapter(): PropertySaveAdapter = chatColorSaveAdapter()
        .with(parserRenderAdapter())

fun defaultLoadAdapter(): PropertyLoadAdapter = chatColorLoadAdapter()
        .with(parserParseAdapter())

fun PropertySaveAdapter.with(adapter: PropertySaveAdapter): PropertySaveAdapter = { instance, property, type, value ->
    val (value, type) = invoke(instance, property, type, value)
    adapter(instance, property, type, value)
}

fun PropertyLoadAdapter.with(adapter: PropertyLoadAdapter): PropertyLoadAdapter = { instance, property, value ->
    adapter(instance, property, invoke(instance, property, value))
}