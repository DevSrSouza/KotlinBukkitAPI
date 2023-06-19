package br.com.devsrsouza.kotlinbukkitapi.extensions

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

public inline fun task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = task(delayToRun, repeatDelay, false, plugin, runnable)

public inline fun Plugin.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = task(delayToRun, repeatDelay, this, runnable)

public inline fun WithPlugin<*>.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = plugin.task(delayToRun, repeatDelay, runnable)

public inline fun taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = task(delayToRun, repeatDelay, true, plugin, runnable)

public inline fun Plugin.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = taskAsync(delayToRun, repeatDelay, this, runnable)

public inline fun WithPlugin<*>.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = plugin.taskAsync(delayToRun, repeatDelay, runnable)

public inline fun task(
        delayToRun: Long,
        repeatDelay: Long = -1,
        async: Boolean,
        plugin: Plugin,
        crossinline runnable: BukkitRunnable.() -> Unit
): BukkitTask = scheduler(runnable).run {
    if (repeatDelay > -1) if (async) runTaskTimerAsynchronously(plugin, delayToRun, repeatDelay) else runTaskTimer(plugin, delayToRun, repeatDelay)
    else if (delayToRun > 0) if (async) runTaskLaterAsynchronously(plugin, delayToRun) else runTaskLater(plugin, delayToRun)
    else if (async) runTaskAsynchronously(plugin) else runTask(plugin)
}

public inline fun scheduler(crossinline runnable: BukkitRunnable.() -> Unit): BukkitRunnable = object : BukkitRunnable() {
    override fun run() {
        this.runnable()
    }
}