package br.com.devsrsouza.kotlinbukkitapi.config

import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.safeCast
import kotlin.reflect.jvm.isAccessible

object ConfigDSLIMPL {

    fun loadAndSetDefault(model: KClass<*>, instance: Any, map: Map<String, Any>,
                          saveTransformer: PropertyTransformer? = null,
                          loadTransformer: PropertyTransformer? = null,
                          set: (Map.Entry<String, Any>) -> Unit) {
        loadAndSetDefaultR(model, instance, map, set, null,
                model.objectInstance === instance, saveTransformer, loadTransformer)
    }

    private fun loadAndSetDefaultR(
            model: KClass<*>,
            instance: Any,
            map: Map<String, Any>,
            set: (Map.Entry<String, Any>) -> Unit,
            base: Entry<String, out Any?>?,
            isObject: Boolean,
            saveTransformer: PropertyTransformer? = null,
            loadTransformer: PropertyTransformer? = null
    ) {

        val properties = model.memberProperties
                .filterIsInstance<KMutableProperty1<Any, Any>>()
                .filter { it.visibility == KVisibility.PUBLIC }
                .filterNot { it.isLateinit }
                .filterNot { it.returnType.isMarkedNullable }

        for (prop in properties) {
            prop.isAccessible = true

            val obj = map.get(prop.name)

            val delegate = prop.getDelegate(instance).let { if (it is Serializable<*>) it as Serializable<Any> else null }

            if (obj != null) {
                if (delegate?.loadFunction != null) {
                    delegate.deserialize(loadTransformer?.invoke(prop, obj) ?: obj)
                } else if (prop.returnType.classifier != null) {
                    val propType = prop.returnType.classifier
                    when (propType) {
                        String::class, Number::class, Boolean::class ->
                            if (obj is String || obj is Number || obj is Boolean) prop.set(instance, loadTransformer?.invoke(prop, obj)
                                    ?: obj)
                        Map::class -> {
                            loadMap(obj, prop, instance, loadTransformer)
                        }
                        List::class -> {
                            loadList(obj, prop, instance, loadTransformer)
                        }
                        else -> {
                            loadRecursive(propType, obj, prop, instance, loadTransformer) { obj, propClass ->
                                if (propClass.objectInstance == prop.get(instance)) {
                                    val insta = prop.get(instance)
                                    loadAndSetDefaultR(insta::class, insta, obj, set, resolveEntry(null, prop.name, base), isObject)
                                } else {
                                    val loadedPojo = loadPojo(propClass, obj, loadTransformer)
                                    prop.set(instance, loadTransformer?.invoke(prop, loadedPojo) ?: loadedPojo)
                                }
                            }
                        }
                    }
                }
            } else {
                setProperty(delegate, set, prop, base, instance, isObject, obj, saveTransformer)
            }
        }
    }

    fun setTo(model: KClass<*>, instance: Any,
              saveTransformer: PropertyTransformer? = null,
              set: (Map.Entry<String, Any>) -> Unit) {
        setToR(model, instance, set, null, model.objectInstance === instance, saveTransformer)
    }

    private fun setToR(
            model: KClass<*>,
            instance: Any,
            set: (Map.Entry<String, Any>) -> Unit,
            base: Entry<String, out Any?>?,
            isObject: Boolean = false,
            saveTransformer: PropertyTransformer? = null
    ) {

        val properties = model.memberProperties
                .filterIsInstance<KMutableProperty1<Any, Any>>()
                .filter { it.visibility == KVisibility.PUBLIC }
                .filterNot { it.isLateinit }
                .filterNot({ it.returnType.isMarkedNullable })

        for (prop in properties) {
            prop.isAccessible = true

            val delegate = prop.getDelegate(instance).let { if (it is Serializable<*>) it as Serializable<Any> else null }

            val obj = prop.getter.apply { isAccessible = true }.call(instance)
            setProperty(delegate, set, prop, base, instance, isObject, obj, saveTransformer)
        }
    }

    private fun setProperty(
            delegate: Serializable<Any>?,
            set: (Map.Entry<String, Any>) -> Unit,
            prop: KMutableProperty1<Any, Any>,
            base: Entry<String, out Any?>?,
            instance: Any,
            isObject: Boolean,
            obj: Any?,
            saveTransformer: PropertyTransformer? = null
    ) {
        if (delegate?.saveFunction != null) {
            val toSave = delegate.serialize()
            set(resolveEntry(saveTransformer?.invoke(prop, toSave)
                    ?: toSave, prop.name, base) as Entry<String, Any>)
        } else {
            val defaultObj = prop.getter.apply { isAccessible = true }.call(instance).let {
                saveTransformer?.invoke(prop, it) ?: it
            }

            when (defaultObj) {
                is String, is Number, is Boolean -> set(resolveEntry(defaultObj, prop.name, base) as Entry<String, Any>)
                is Map<*, *> -> {
                    if (isMapCompatible(prop, defaultObj) && defaultObj.keys.isNotEmpty()) {
                        val map = defaultObj as Map<String, Any>
                        if (isMapValuePrimitive(prop, defaultObj)) {
                            map.forEach { k, v ->
                                set(resolveEntry(Entry(k, v), prop.name, base) as Entry<String, Any>)
                            }
                        } else {
                            map.forEach { k, v ->
                                setToR(v::class, v, set, resolveEntry(Entry(k, null), prop.name, base), saveTransformer = saveTransformer)
                            }
                        }
                    }
                }
                is List<*> -> {
                    if (defaultObj.isNotEmpty())
                        if (isPrimitiveList(prop, defaultObj))
                            set(resolveEntry(defaultObj, prop.name, base) as Entry<String, Any>)
                        else {
                            val list = defaultObj as List<Any>
                            var count = 1
                            list.forEach { v ->
                                setToR(v::class, v, set, resolveEntry(Entry(count.toString(), null), prop.name, base), saveTransformer = saveTransformer)
                                count++
                            }
                        }
                }
                is KClass<*> -> {
                    val objectI = defaultObj.objectInstance
                    if (isObject) {
                        var newInstance = objectI ?: defaultObj.createInstance()
                        setToR(newInstance::class, newInstance, set, resolveEntry(null, prop.name, base),
                                objectI == newInstance, saveTransformer)
                    } else {
                        if (objectI == null) {
                            var newInstance = defaultObj.createInstance()
                            setToR(newInstance::class, newInstance, set, resolveEntry(null, prop.name, base),
                                    saveTransformer = saveTransformer)
                        }
                    }
                }
                else -> {
                    if (isObject) {
                        setToR(
                                defaultObj::class,
                                defaultObj,
                                set,
                                resolveEntry(null, prop.name, base),
                                defaultObj == defaultObj::class.objectInstance,
                                saveTransformer
                        )
                    } else {
                        if (defaultObj != defaultObj::class.objectInstance) {
                            setToR(
                                    defaultObj::class, defaultObj, set,
                                    resolveEntry(null, prop.name, base),
                                    saveTransformer = saveTransformer
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadPojo(
            model: KClass<*>,
            map: Map<String, Any>,
            loadTransformer: PropertyTransformer? = null): Any {

        var instance = model.createInstance()

        val properties = model.memberProperties
                .filterIsInstance<KMutableProperty1<Any, Any>>()
                .filter { it.visibility == KVisibility.PUBLIC }
                .filterNot { it.isLateinit }
                .filterNot { it.returnType.isMarkedNullable }

        for (prop in properties) {

            prop.isAccessible = true

            val obj = map.get(prop.name)

            val delegate = prop.getDelegate(instance).let { if (it is Serializable<*>) it as Serializable<Any> else null }

            if (obj != null) {

                if (delegate?.loadFunction != null) {
                    delegate.deserialize(loadTransformer?.invoke(prop, obj) ?: obj)
                } else if (prop.returnType.classifier != null) {
                    val propType = prop.returnType.classifier
                    when (propType) {
                        String::class, Number::class, Boolean::class ->
                            if (obj is String || obj is Number || obj is Boolean) prop.set(instance, loadTransformer?.invoke(prop, obj)
                                    ?: obj)
                        Map::class -> {
                            loadMap(obj, prop, instance, loadTransformer)
                        }
                        List::class -> {
                            loadList(obj, prop, instance, loadTransformer)
                        }
                        else -> {
                            loadRecursive(propType, obj, prop, instance) { obj, propClass ->
                                val pojoLoaded = loadPojo(propClass, obj, loadTransformer)
                                prop.set(instance, loadTransformer?.invoke(prop, pojoLoaded) ?: pojoLoaded)
                            }
                        }
                    }
                }
            }
        }

        return instance
    }

    private fun loadList(
            obj: Any?,
            prop: KMutableProperty1<Any, Any>,
            instance: Any,
            loadTransformer: PropertyTransformer? = null
    ) {
        if (obj is List<*> && isPrimitiveList(prop, obj)) {
            prop.set(instance, loadTransformer?.invoke(prop, obj) ?: obj)
        } else if (obj is Map<*, *>) {
            val typeClass = KClass::class.safeCast(
                    prop.returnType.arguments.getOrNull(0)
                            ?.type?.takeUnless { it.isMarkedNullable }?.classifier
            )
            if (typeClass != null) {
                val list = mutableListOf<Any>()

                obj.forEach { _, v ->
                    if (v is Map<*, *> && isMapCompatible(v)) {
                        list.add(loadPojo(typeClass, v as Map<String, Any>, loadTransformer))
                    }
                }
                prop.set(instance, loadTransformer?.invoke(prop, list) ?: list)
            }
        }
    }

    private fun loadMap(
            obj: Any?,
            prop: KMutableProperty1<Any, Any>,
            instance: Any,
            loadTransformer: PropertyTransformer? = null
    ) {
        if (obj is Map<*, *> && isMapCompatible(obj))
            if (isMapValuePrimitive(prop, obj)) {
                prop.set(instance, loadTransformer?.invoke(prop, obj) ?: obj)
            } else {
                val valueTypeClass = KClass::class.safeCast(
                        prop.returnType.arguments.getOrNull(1)
                                ?.type?.takeUnless { it.isMarkedNullable }?.classifier
                )
                if (valueTypeClass != null) {
                    val mapLoaded = obj.mapValues {
                        it.value.let {
                            if (it is Map<*, *> && isMapCompatible(it)) {
                                loadPojo(valueTypeClass, it as Map<String, Any>, loadTransformer)
                            } else null
                        }
                    }.filter { it.key != null && it.value != null } as Map<Any, Any>
                    prop.apply { isAccessible = true }.set(instance, loadTransformer?.invoke(prop, mapLoaded)
                            ?: mapLoaded)
                }
            }
    }

    private fun loadRecursive(
            propType: KClassifier?,
            obj: Any,
            prop: KMutableProperty1<Any, Any>,
            instance: Any,
            loadTransformer: PropertyTransformer? = null,
            block: (obj: Map<String, Any>, propClass: KClass<*>) -> Unit
    ) {
        val propClass = KClass::class.safeCast(propType)
        if (propClass != null)
            if (obj is Map<*, *>) {
                if (isMapCompatible(obj)) {
                    block(obj as Map<String, Any>, propClass)
                }
            } else if (propClass.isInstance(obj)) {
                prop.set(instance, loadTransformer?.invoke(prop, obj) ?: obj)
            }
    }

    private fun resolveEntry(
            value: Any?,
            name: String,
            base: Entry<String, out Any?>?
    ): Entry<String, Any?> {
        return base?.copy()?.apply {
            if (this.value != null && this.value is Entry<*, *>) {
                var actualEntry = this.value as Entry<String, Any?>
                while (true) {
                    if (actualEntry.value is Map.Entry<*, *>)
                        actualEntry = actualEntry.value as Entry<String, Any?>
                    else {
                        actualEntry.value = Entry(name, resolveEntry@ value)
                        break
                    }
                }
            } else {
                this.value = Entry(name, resolveEntry@ value)
            }
        } ?: Entry(name, value)
    }

    private fun isMapCompatible(map: Map<*, *>) = map.keys.find { it !is String } == null
    private fun isMapValuePrimitive(map: Map<*, *>) = map.values.find {
        it !is String && it !is Number && it !is Boolean &&
                (it is List<*> && !isPrimitiveList(it))
    } == null

    private fun isPrimitiveList(list: List<*>) = list.find { it !is String && it !is Number && it !is Boolean } == null

    private fun isMapCompatible(property: KProperty<*>, map: Map<*, *>) = property.run {

        val type = returnType
        if (type.arguments.isNotEmpty()) {
            val keyType = type.arguments.getOrNull(0)//Class::class.safeCast(type.actualTypeArguments.getOrNull(0))
            if (keyType != null && keyType.type?.isMarkedNullable == false) {
                return keyType.type?.classifier.let { it == String::class } &&
                        map.keys.find { it !is String } == null
            } else false
        } else false
    }

    private fun isMapValuePrimitive(property: KProperty<*>, map: Map<*, *>) = property.run {

        val type = returnType
        if (type.arguments.isNotEmpty()) {
            val valueKey = type.arguments.getOrNull(1)
            if (valueKey != null && valueKey.type?.isMarkedNullable == false) {
                val valueClass = KClass::class.safeCast(valueKey.type?.takeUnless { it.isMarkedNullable }?.classifier)
                if (valueClass != null) {
                    if (valueClass == String::class ||
                            valueClass == Number::class ||
                            valueClass == Boolean::class) {
                        map.values.find { v -> !valueClass.isInstance(v) } == null
                    } else if (valueClass.isSubclassOf(List::class)) {
                        map.values.find { !isPrimitiveList(valueKey.type!!, it as List<*>) } == null
                    } else false
                } else false
            } else false
        } else false
    }

    private fun isPrimitiveList(property: KProperty<*>, list: List<*>) = isPrimitiveList(property.returnType, list)
    private fun isPrimitiveList(type: KType, list: List<*>) =
            if (KClass::class.safeCast(type.classifier)?.isSubclassOf(List::class) == true &&
                    List::class.isInstance(list)) {
                val typeClass = KClass::class.safeCast(type.arguments.getOrNull(0)?.type?.takeUnless { it.isMarkedNullable }?.classifier)
                if (typeClass != null) {
                    if (typeClass == String::class || typeClass == Number::class ||
                            typeClass == Boolean::class) {
                        (list).find { !typeClass.isInstance(it) } == null
                    } else false
                } else false
            } else false

    private class Entry<K, V>(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            return value.apply { value = newValue }
        }

        fun copy(): Entry<K, Any?> {
            val copy = Entry(key, value as Any?)
            if (value is Entry<*, *>) {
                copy.value = (value as Entry<K, Any?>).copy()
            }
            return copy
        }
    }
}