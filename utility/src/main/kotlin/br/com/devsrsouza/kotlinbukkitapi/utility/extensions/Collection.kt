package br.com.devsrsouza.kotlinbukkitapi.utility.extensions

public fun Collection<String>.containsIgnoreCase(
        element: String
): Boolean = any { it.equals(element, true) }

public fun <T> MutableCollection<T>.clear(onRemove: (T) -> Unit) {
    toMutableList().forEach {
        remove(it)
        onRemove(it)
    }
}

public fun Array<String>.containsIgnoreCase(
        element: String
): Boolean = any { it.equals(element, true) }

public fun <V> Map<String, V>.containsKeyIgnoreCase(
        key: String
): Boolean = keys.containsIgnoreCase(key)

public fun <V> Map<String, V>.getIgnoreCase(
        key: String
): V? = entries.find { it.key.equals(key, true) }?.value

public fun <K, V> MutableMap<K, V>.clear(onRemove: (K, V) -> Unit) {
    keys.toMutableSet().forEach { onRemove(it, remove(it)!!) }
}