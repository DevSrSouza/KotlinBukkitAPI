package br.com.devsrsouza.kotlinbukkitapi.config

import kotlin.reflect.*
import kotlin.reflect.full.*

fun publicMutablePropertiesFrom(clazz: KClass<*>) = clazz.memberProperties
        .filterIsInstance<KMutableProperty1<Any, Any>>()
        .filter { it.getter.visibility == KVisibility.PUBLIC }
        .filterNot { it.isLateinit }
        .filterNot { it.returnType.isMarkedNullable } // TODO usar nullable

inline fun <reified T> KMutableProperty1<Any, Any>.getDelegateFromType(instance: Any): T? {
    return getDelegate(instance) as? T
}

fun primitiveTypes() = listOf(String::class, Boolean::class,
        Byte::class, Short::class, Int::class, Long::class, Float::class, Double::class
)

val KType.genericTypes get() = arguments
val KType.genericTypesOrNull get() = genericTypes.takeIf { it.isNotEmpty() }
val KType.isPrimitive: Boolean get() = primitiveTypes().any { it == classifier }
fun KType.isSubclassOf(klass: KClass<*>) = (classifier as? KClass<*>)?.isSubclassOf(klass) == true
val KType.isList: Boolean get() = isSubclassOf(List::class)
val KType.isMutableList: Boolean get() = isSubclassOf(MutableList::class)
val KType.isMap: Boolean get() = isSubclassOf(Map::class)
val KType.isMutableMap: Boolean get() = isSubclassOf(MutableMap::class)

val KTypeProjection.kclass: KClass<*>? get() = type?.classifier as? KClass<*>
val KType.firstGenericType: KTypeProjection? get() = genericTypes.getOrNull(0)
val KType.secondGenericType: KTypeProjection? get() = genericTypes.getOrNull(1)

val KType.isFirstGenericString: Boolean get() = firstGenericType?.kclass == String::class
val KType.isFirstGenericPrimitive: Boolean get() = firstGenericType?.type?.isPrimitive == true
val KType.isSecondGenericPrimitive: Boolean get() = secondGenericType?.type?.isPrimitive == true

fun fixNumberType(type: KType, any: Number): Number {
    return when(type.classifier) {
        Byte::class -> any.toByte()
        Short::class -> any.toShort()
        Int::class -> any.toInt()
        Long::class -> any.toLong()
        Float::class -> any.toFloat()
        Double::class -> any.toDouble()
        else -> any
    }
}

val KType.isEnum: Boolean get() = isSubclassOf(Enum::class)

fun getEnumValues(enumClass: KClass<Enum<*>>): Array<Enum<*>> = enumClass.java.enumConstants

fun getEnumValueByName(enumClass: KClass<Enum<*>>, name: String): Any? {
    return getEnumValues(enumClass).firstOrNull { it.name.equals(name, true) }
}