package br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.CoroutineTask
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

inline fun task(delayToRun: Long = 0,
                plugin: Plugin,
                crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = task(delayToRun, false, plugin, controller)

inline fun Plugin.task(delayToRun: Long = 0,
                       crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = task(delayToRun, this, controller)

inline fun WithPlugin<*>.task(delayToRun: Long = 0,
                       crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = plugin.task(delayToRun, controller)

inline fun taskAsync(delayToRun: Long = 0,
                     plugin: Plugin,
                     crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = task(delayToRun, true, plugin, controller)

inline fun Plugin.taskAsync(delayToRun: Long = 0,
                            crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = taskAsync(delayToRun, this, controller)

inline fun WithPlugin<*>.taskAsync(delayToRun: Long = 0,
                            crossinline controller: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask = plugin.taskAsync(delayToRun, controller)

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