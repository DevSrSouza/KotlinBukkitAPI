package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.*
import org.bukkit.entity.Player

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

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * and when the [player] disconnect the server removing the possibility of Job leaks.
 */
fun LifecycleListener<*>.playerCoroutineScope(player: Player): CoroutineScope {
    return plugin.playerCoroutineScope(player)
}

fun KotlinPlugin.playerCoroutineScope(player: Player): CoroutineScope {
    return getOrInsertCoroutineLifecycle().getPlayerCoroutineScope(player)
}

private fun KotlinPlugin.getOrInsertCoroutineLifecycle(): CoroutineLifecycle {
    return getOrInsertGenericLifecycle(Int.MIN_VALUE) {
        CoroutineLifecycle(this)
    }
}

internal class CoroutineLifecycle(
        override val plugin: KotlinPlugin
) : LifecycleListener<KotlinPlugin> {

    inner class PlayerCoroutineScope(
        val job: Job,
        val coroutineScope: CoroutineScope
    ) {
        fun cancelJobs() = job.cancel()
    }

    private val job = Job()
    val pluginCoroutineScope = CoroutineScope(BukkitDispatchers.SYNC + job)

    private val playersCoroutineScope by lazy {
        onlinePlayerMapOf<PlayerCoroutineScope>()
    }

    override fun onPluginEnable() {}

    override fun onPluginDisable() {
        job.cancel()
        for (scopes in playersCoroutineScope.values) {
            scopes.cancelJobs()
        }
    }

    fun getPlayerCoroutineScope(player: Player): CoroutineScope {
        return playersCoroutineScope[player]?.coroutineScope
            ?: newPlayerCoroutineScope().also {
                playersCoroutineScope.put(player, it) { playerCoroutineScope ->
                    playerCoroutineScope.cancelJobs()
                }
            }.coroutineScope
    }

    private fun newPlayerCoroutineScope(): PlayerCoroutineScope {
        val job = Job()
        return PlayerCoroutineScope(
            job,
            CoroutineScope(BukkitDispatchers.SYNC + job)
        )
    }
}