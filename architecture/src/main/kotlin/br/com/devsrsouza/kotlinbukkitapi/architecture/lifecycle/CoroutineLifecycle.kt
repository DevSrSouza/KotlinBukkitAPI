package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.*

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default
 *
 * This scope ensures that your task will complete before the plugin disable doing
 * a [Job.join] in all your active jobs.
 *
 * NOTE: If you have a infinity task this will stop the plugin from disable,
 * even could block the server, if you have a repeating task that never ends
 * you should use [cancellableCoroutineScope].
 */
val LifecycleListener<*>.coroutineScope: CoroutineScope
    get() = plugin.coroutineScope

val KotlinPlugin.coroutineScope: CoroutineScope
    get() = getOrInsertCoroutineLifecycle().coroutineScope

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * removing the possibility of Job leaks.
 */
val LifecycleListener<*>.cancellableCoroutineScope: CoroutineScope
    get() = plugin.cancellableCoroutineScope

val KotlinPlugin.cancellableCoroutineScope: CoroutineScope
    get() = getOrInsertCoroutineLifecycle().cancellableCoroutineScope

private fun KotlinPlugin.getOrInsertCoroutineLifecycle(): CoroutineLifecycle {
    return getOrInsertGenericLifecycle(Int.MIN_VALUE) {
        CoroutineLifecycle(this)
    }
}

internal class CoroutineLifecycle(
        override val plugin: KotlinPlugin
) : LifecycleListener<KotlinPlugin> {

    private val job = Job()
    private val cancelableJob = Job()

    val coroutineScope = CoroutineScope(BukkitDispatchers.SYNC + job)
    val cancellableCoroutineScope = CoroutineScope(BukkitDispatchers.SYNC + cancelableJob)

    override fun onPluginEnable() {}

    override fun onPluginDisable() {
        if(cancelableJob.isActive)
            cancelableJob.cancel()

        if(job.isActive) {
            runBlocking {
                job.join()
            }
        }
    }
}