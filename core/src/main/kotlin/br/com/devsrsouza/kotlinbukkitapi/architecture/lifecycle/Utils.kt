package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin

inline fun <reified T : LifecycleListener<KotlinPlugin>> KotlinPlugin.getOrInsertGenericLifecycle(
        priority: Int,
        factory: () -> T
): T {
    return lifecycleListeners
            .map { it.listener }
            .filterIsInstance<T>()
            .firstOrNull()
            ?: lifecycle(priority) { factory() }
}