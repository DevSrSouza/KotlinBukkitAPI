package br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.CoroutineTask
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

inline fun task(delayToRun: Long = 0,
                plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = task(delayToRun, false, plugin, controller)

inline fun taskAsync(delayToRun: Long = 0,
                     plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                     crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = task(delayToRun, true, plugin, controller)

inline fun task(delayToRun: Long,
                async: Boolean,
                plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = plugin.schedule(if (async) SynchronizationContext.ASYNC else SynchronizationContext.SYNC) {
    if (delayToRun > 0) waitFor(delayToRun)
    controller()
}

inline fun scheduler(crossinline runnable: BukkitRunnable.() -> Unit) = object : BukkitRunnable() {
    override fun run() {
        this.runnable()
    }
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