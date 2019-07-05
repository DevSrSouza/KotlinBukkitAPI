package br.com.devsrsouza.kotlinbukkitapi.config

import br.com.devsrsouza.kotlinbukkitapi.config.adapter.chatColorLoadAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.chatColorSaveAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.parserParseAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.adapter.parserRenderAdapter
import kotlin.reflect.KMutableProperty1

typealias PropertyAdapter = (instance: Any, property: KMutableProperty1<Any, Any>, value: Any) -> Any

fun emptyAdapter(): PropertyAdapter = { _, _, value -> value }

fun defaultLoadAdapter(): PropertyAdapter = chatColorLoadAdapter()
        .with(parserParseAdapter())

fun defaultSaveAdapter(): PropertyAdapter = chatColorSaveAdapter()
        .with(parserRenderAdapter())

fun PropertyAdapter.with(adapter: PropertyAdapter): PropertyAdapter = { instance, property, value ->
    adapter(instance, property, invoke(instance, property, value))
}
