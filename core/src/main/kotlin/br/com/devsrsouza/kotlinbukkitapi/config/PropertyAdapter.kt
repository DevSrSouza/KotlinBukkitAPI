package br.com.devsrsouza.kotlinbukkitapi.config

import kotlin.reflect.KMutableProperty1

typealias PropertyAdapter = (instance: Any, property: KMutableProperty1<Any, Any>, value: Any) -> Any

fun emptyAdapter(): PropertyAdapter = { _, _, value -> value }

fun PropertyAdapter.with(adapter: PropertyAdapter): PropertyAdapter = { instance, property, value ->
    adapter(instance, property, invoke(instance, property, value))
}
