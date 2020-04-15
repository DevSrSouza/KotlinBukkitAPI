package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.*

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * removing the possibility of Job leaks.
 */
val LifecycleListener<*>.pluginCoroutineScope: CoroutineScope
    get() = plugin.pluginCoroutineScope

val KotlinPlugin.pluginCoroutineScope: CoroutineScope
    get() = getOrInsertCoroutineLifecycle().pluginCoroutineScope

private fun KotlinPlugin.getOrInsertCoroutineLifecycle(): CoroutineLifecycle {
    return getOrInsertGenericLifecycle(Int.MIN_VALUE) {
        CoroutineLifecycle(this)
    }
}

internal class CoroutineLifecycle(
        override val plugin: KotlinPlugin
) : LifecycleListener<KotlinPlugin> {

    private val job = Job()

    val pluginCoroutineScope = CoroutineScope(BukkitDispatchers.SYNC + job)

    override fun onPluginEnable() {}

    override fun onPluginDisable() {
        job.cancel()
    }
}