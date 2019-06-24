package br.com.devsrsouza.kotlinbukkitapi.utils

fun Collection<String>.containsIgnoreCase(
        element: String
) = any { it.equals(element, true) }

fun Array<String>.containsIgnoreCase(
        element: String
) = any { it.equals(element, true) }

fun <V> Map<String, V>.containsKeyIgnoreCase(
        key: String
) = keys.containsIgnoreCase(key)