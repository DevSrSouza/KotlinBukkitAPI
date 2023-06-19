package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin

public inline fun <reified T : PluginLifecycleListener> KotlinPlugin.getOrInsertGenericLifecycle(
    priority: Int,
    factory: () -> T,
): T {
    return lifecycleListeners
        .map { it.listener }
        .filterIsInstance<T>()
        .firstOrNull()
        ?: factory().also { registerKotlinPluginLifecycle(priority, it) }
}
