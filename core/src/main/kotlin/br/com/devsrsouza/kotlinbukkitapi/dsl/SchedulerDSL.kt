package br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

inline fun scheduler(crossinline runnable: BukkitRunnable.() -> Unit) = object : BukkitRunnable() {
    override fun run() {
        this.runnable()
    }
}

inline fun task(delayToRun: Long = 0,
                repeatDelay: Long = -1,
                plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                crossinline runnable: BukkitRunnable.() -> Unit)
        = task(delayToRun, repeatDelay, false, plugin, runnable)

inline fun taskAsync(delayToRun: Long = 0,
                     repeatDelay: Long = -1,
                     plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                     crossinline runnable: BukkitRunnable.() -> Unit)
        = task(delayToRun, repeatDelay, true, plugin, runnable)

inline fun task(delayToRun: Long,
                repeatDelay: Long = -1,
                async: Boolean,
                plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                crossinline runnable: BukkitRunnable.() -> Unit) = scheduler(runnable).run {
    if (repeatDelay > -1) if (async) runTaskTimerAsynchronously(repeatDelay, delayToRun, plugin) else runTaskTimer(repeatDelay, delayToRun, plugin)
    else if (delayToRun > 0) if (async) runTaskLaterAsynchronously(delayToRun, plugin) else runTaskLater(delayToRun, plugin)
    else if (async) runTaskAsynchronously(plugin) else runTask(plugin)
}

fun BukkitRunnable.runTask(plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTask(plugin)

fun BukkitRunnable.runTaskAsynchronously(plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTaskAsynchronously(plugin)

fun BukkitRunnable.runTaskLater(delay: Long, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTaskLater(plugin, delay)

fun BukkitRunnable.runTaskLaterAsynchronously(delay: Long, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTaskLaterAsynchronously(plugin, delay)

fun BukkitRunnable.runTaskTimer(repeatDelay: Long, delayToStart: Long = 0,
                                plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTaskTimer(plugin, delayToStart, repeatDelay)

fun BukkitRunnable.runTaskTimerAsynchronously(repeatDelay: Long, delayToStart: Long = 0,
                                              plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = runTaskTimerAsynchronously(plugin, delayToStart, repeatDelay)