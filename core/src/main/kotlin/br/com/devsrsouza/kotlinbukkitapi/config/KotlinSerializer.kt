package br.com.devsrsouza.kotlinbukkitapi.config

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible

object KotlinSerializer {

    fun instanceToMap(instance: Any, adapter: PropertyAdapter): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        val clazz = instance::class
        val properties = publicMutablePropertiesFrom(clazz)
        loop@ for (prop in properties) {
            fun put(any: Any, key: String = prop.name) {
                map.put(key, adapter(instance, prop, any))
            }

            val obj = prop.get(instance)
            when {
                prop.isPrimitive -> put(obj)
                isListPrimitive(prop) -> put(obj)
                isList(prop) -> {
                    val list = obj as List<Any>
                    val map = list.withIndex().associate { (key, value) ->
                        key.toString() to instanceToMap(value, adapter)
                    }
                    put(map)
                }
                isMapKeyString(prop) && isMapValuePrimitive(prop) -> put(obj)
                isMapKeyString(prop) -> {
                    val map = obj as Map<String, Any>
                    for ((key, value) in map) {
                        put(instanceToMap(value, adapter), key)
                    }
                }
                else -> put(instanceToMap(obj, adapter))
            }
        }

        return map
    }

    fun <T : Any> mapToInstance(type: KClass<T>, map: Map<String, Any>, adapter: PropertyAdapter): T {
        val instance = type.objectInstance ?: type.createInstance()

        val properties = publicMutablePropertiesFrom(type)
        loop@ for (prop in properties) {
            fun set(any: Any) = prop.set(instance, adapter(instance, prop, any))
            val obj = map.get(prop.name) ?: continue@loop

            prop.isAccessible = true

            when {
                prop.isPrimitive -> set(obj)
                isListPrimitive(prop) -> set(obj)
                isList(prop) -> {
                    val type = getListTypeClass(prop) ?: continue@loop
                    val list = obj as Map<String, Any>

                    set(list.values.map { mapToInstance(type, it as Map<String, Any>, adapter) })
                }
                isMapKeyString(prop) && isMapValuePrimitive(prop) -> set(obj)
                isMapKeyString(prop) -> {
                    val type = getListTypeClass(prop) ?: continue@loop
                    val map = obj as Map<String, Any>

                    set(map.mapValues { mapToInstance(type, it as Map<String, Any>, adapter) })
                }
                else -> {
                    val type = prop.returnTypeClass() ?: continue@loop
                    set(mapToInstance(type, obj as Map<String, Any>, adapter))
                }
            }
        }

        return instance
    }
}