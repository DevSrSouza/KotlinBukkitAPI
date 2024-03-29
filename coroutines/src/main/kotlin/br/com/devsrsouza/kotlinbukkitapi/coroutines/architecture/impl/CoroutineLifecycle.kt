package br.com.devsrsouza.kotlinbukkitapi.coroutines.architecture.impl

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.LifecycleListener
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.getOrInsertGenericLifecycle
import br.com.devsrsouza.kotlinbukkitapi.coroutines.BukkitDispatchers
import br.com.devsrsouza.kotlinbukkitapi.utility.collections.onlinePlayerMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.bukkit.entity.Player

internal fun KotlinPlugin.getOrInsertCoroutineLifecycle(): CoroutineLifecycle {
    return getOrInsertGenericLifecycle(Int.MIN_VALUE) {
        CoroutineLifecycle(this)
    }
}

internal class CoroutineLifecycle(
    override val plugin: KotlinPlugin,
) : LifecycleListener<KotlinPlugin> {

    inner class PlayerCoroutineScope(
        val job: Job,
        val coroutineScope: CoroutineScope,
    ) {
        fun cancelJobs() = job.cancel()
    }

    private val job = SupervisorJob()
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
        val job = SupervisorJob()
        return PlayerCoroutineScope(
            job,
            CoroutineScope(BukkitDispatchers.SYNC + job),
        )
    }
}
