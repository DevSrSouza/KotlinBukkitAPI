package br.com.devsrsouza.kotlinbukkitapi.coroutines

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.isActive
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.lang.Runnable
import kotlin.coroutines.CoroutineContext

public val WithPlugin<*>.BukkitDispatchers: PluginDispatcher get() = plugin.BukkitDispatchers

public val Plugin.BukkitDispatchers: PluginDispatcher get() = PluginDispatcher(this as JavaPlugin)

@JvmInline
public value class PluginDispatcher(private val plugin: JavaPlugin) {
    public val ASYNC: BukkitDispatcher get() = BukkitDispatcher(plugin, true)
    public val SYNC: BukkitDispatcher get() = BukkitDispatcher(plugin, false)
}

private val bukkitScheduler
    get() = Bukkit.getScheduler()

@OptIn(InternalCoroutinesApi::class)
public class BukkitDispatcher(
    public val plugin: JavaPlugin,
    public val async: Boolean = false,
) : CoroutineDispatcher(), Delay {

    private val runTaskLater: (Plugin, Runnable, Long) -> BukkitTask =
        if (async) {
            bukkitScheduler::runTaskLaterAsynchronously
        } else {
            bukkitScheduler::runTaskLater
        }
    private val runTask: (Plugin, Runnable) -> BukkitTask =
        if (async) {
            bukkitScheduler::runTaskAsynchronously
        } else {
            bukkitScheduler::runTask
        }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = runTaskLater(
            plugin,
            Runnable {
                continuation.apply { resumeUndispatched(Unit) }
            },
            timeMillis / 50,
        )
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) {
            return
        }

        if (!async && Bukkit.isPrimaryThread()) {
            block.run()
        } else {
            runTask(plugin, block)
        }
    }
}

public fun JavaPlugin.dispatcher(async: Boolean = false): BukkitDispatcher = BukkitDispatcher(this, async)
