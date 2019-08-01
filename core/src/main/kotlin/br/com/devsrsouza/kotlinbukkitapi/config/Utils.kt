package br.com.devsrsouza.kotlinbukkitapi.config

import kotlin.reflect.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

fun publicMutablePropertiesFrom(clazz: KClass<*>) = clazz.memberProperties
        .filterIsInstance<KMutableProperty1<Any, Any>>()
        .filter { it.getter.visibility == KVisibility.PUBLIC }
        .filterNot { it.isLateinit }
        .filterNot { it.returnType.isMarkedNullable } // TODO usar nullable

inline fun <reified T> KMutableProperty1<Any, Any>.getDelegateFromType(instance: Any): T? {
    return getDelegate(instance) as? T
}

fun primitiveTypes() = listOf(String::class, Boolean::class,
        Byte::class, Short::class, Int::class, Long::class, Float::class, Double::class)

fun KMutableProperty1<*, *>.returnTypeClass(): KClass<*>? {
    val type = returnType.classifier ?: return null
    return type as? KClass<*>
}

fun KMutableProperty1<*, *>.isReturnTypeSubclassOf(clazz: KClass<*>): Boolean {
    val typeClazz = returnTypeClass() ?: return false

    return typeClazz.isSubclassOf(clazz)
}

val KMutableProperty1<*, *>.genericTypes get() = returnType.arguments

val KMutableProperty1<*, *>.genericTypesOrNull get() = genericTypes.takeIf { it.isNotEmpty() }

val KMutableProperty1<*, *>.hasGenericTypes get() = genericTypes.isNotEmpty()

val KMutableProperty1<*, *>.hasStarGenericType get() = genericTypes.any {
    it.type == null && it.variance == null
}

val KTypeProjection.kclass: KClass<*>? get() = type?.classifier as? KClass<*>

val KClass<*>.isPrimitive: Boolean get() {
    return primitiveTypes().any { it == this }
}

val KMutableProperty1<*, *>.isPrimitive: Boolean get() {
    val clazz = returnTypeClass() ?: return false
    return primitiveTypes().any { it == clazz }
}

fun isList(property: KMutableProperty1<*, *>): Boolean {
    return property.isReturnTypeSubclassOf(List::class)
}

fun isMutableList(property: KMutableProperty1<*, *>): Boolean {
    return property.isReturnTypeSubclassOf(MutableList::class)
}

fun getListTypeClass(property: KMutableProperty1<*, *>): KClass<*>? {
    if(isList(property)) {
        val parameters = property.genericTypesOrNull
                ?: return null

        val projection = parameters[0]

        return projection.kclass
    } else return null
}

fun isListString(property: KMutableProperty1<*, *>): Boolean {
    return getListTypeClass(property) == String::class
}

fun isListPrimitive(property: KMutableProperty1<*, *>): Boolean {
    return getListTypeClass(property)?.isPrimitive == true
}

fun isMap(property: KMutableProperty1<*, *>): Boolean {
    return property.isReturnTypeSubclassOf(Map::class)
}

fun isMutableMap(property: KMutableProperty1<*, *>): Boolean {
    return property.isReturnTypeSubclassOf(MutableMap::class)
}

fun getMapKeyClass(property: KMutableProperty1<*, *>): KClass<*>? {
    if(isMap(property)) {
        val parameters = property.genericTypesOrNull
                ?: return null

        val projection = parameters[0]

        return projection.kclass
    } else return null
}

fun isMapKeyString(property: KMutableProperty1<*, *>): Boolean {
    return getMapKeyClass(property) == String::class
}

fun getMapValueClass(property: KMutableProperty1<*, *>): KClass<*>? {
    if(isMap(property)) {
        val parameters = property.genericTypesOrNull
                ?: return null

        val projection = parameters.getOrNull(1) ?: return null

        return projection.kclass
    } else return null
}

fun isMapValuePrimitive(property: KMutableProperty1<*, *>): Boolean {
    return getMapValueClass(property)?.isPrimitive != null
}

fun fixNumberType(property: KMutableProperty1<*, *>, any: Number): Number {
    return when(property.returnTypeClass()) {
        Byte::class -> any.toByte()
        Short::class -> any.toShort()
        Int::class -> any.toInt()
        Long::class -> any.toLong()
        Float::class -> any.toFloat()
        Double::class -> any.toDouble()
        else -> any
    }
}