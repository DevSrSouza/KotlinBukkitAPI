package br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

inline fun task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
) = task(delayToRun, repeatDelay, false, plugin, runnable)

inline fun Plugin.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
) = task(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
) = plugin.task(delayToRun, repeatDelay, runnable)

inline fun taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
) = task(delayToRun, repeatDelay, true, plugin, runnable)

inline fun Plugin.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
) = taskAsync(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
) = plugin.taskAsync(delayToRun, repeatDelay, runnable)

inline fun task(
        delayToRun: Long,
        repeatDelay: Long = -1,
        async: Boolean,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
) = scheduler(runnable).run {
    if (repeatDelay > -1) if (async) runTaskTimerAsynchronously(plugin, repeatDelay, delayToRun) else runTaskTimer(plugin, repeatDelay, delayToRun)
    else if (delayToRun > 0) if (async) runTaskLaterAsynchronously(plugin, delayToRun) else runTaskLater(plugin, delayToRun)
    else if (async) runTaskAsynchronously(plugin) else runTask(plugin)
}

inline fun scheduler(crossinline runnable: BukkitRunnable.() -> Unit) = object : BukkitRunnable() {
    override fun run() {
        this.runnable()
    }
}