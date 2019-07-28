package br.com.devsrsouza.kotlinbukkitapi.utils

fun Collection<String>.containsIgnoreCase(
        element: String
): Boolean = any { it.equals(element, true) }

fun Array<String>.containsIgnoreCase(
        element: String
): Boolean = any { it.equals(element, true) }

fun <V> Map<String, V>.containsKeyIgnoreCase(
        key: String
): Boolean = keys.containsIgnoreCase(key)

fun <V> Map<String, V>.getIgnoreCase(
        key: String
): V? = entries.find { it.key.equals(key, true) }?.value