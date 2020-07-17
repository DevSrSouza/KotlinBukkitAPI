package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin

inline fun <reified T : LifecycleListener<KotlinPlugin>> KotlinPlugin.getOrInsertGenericLifecycle(
        priority: Int,
        factory: () -> T
): T {
    val lifecycle = lifecycleListeners.find {
        T::class.isInstance(it.listener)
    }

    return if(lifecycle != null) {
        lifecycle.listener as T
    } else {
        lifecycle(priority) { factory() }
    }
}