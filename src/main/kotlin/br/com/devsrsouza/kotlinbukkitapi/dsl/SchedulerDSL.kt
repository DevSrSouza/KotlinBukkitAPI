package br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.scheduler.BukkitRunnable

inline fun scheduler(crossinline runnable: BukkitRunnable.() -> Unit) = object : BukkitRunnable() {
    override fun run() {
        this.runnable()
    }
}

inline fun task(delayToRun: Long = 0,
                repeatDelay: Long = -1,
                crossinline runnable: BukkitRunnable.() -> Unit) = task(delayToRun, repeatDelay, false, runnable)

inline fun taskAsync(delayToRun: Long = 0,
                     repeatDelay: Long = -1,
                     crossinline runnable: BukkitRunnable.() -> Unit) = task(delayToRun, repeatDelay, true, runnable)

inline fun task(delayToRun: Long,
                        repeatDelay: Long = -1,
                        async: Boolean,
                        crossinline runnable: BukkitRunnable.() -> Unit) = scheduler(runnable).run {
    if(repeatDelay > -1) if(async) runTaskTimerAsynchronously(repeatDelay, delayToRun) else runTaskTimer(repeatDelay, delayToRun)
    else if(delayToRun > 0) if(async) runTaskLaterAsynchronously(delayToRun) else runTaskLater(delayToRun)
    else if(async) runTaskAsynchronously() else runTask()
}

fun BukkitRunnable.runTask()
        = runTask(KotlinBukkitAPI.INSTANCE)

fun BukkitRunnable.runTaskAsynchronously()
        = runTaskAsynchronously(KotlinBukkitAPI.INSTANCE)

fun BukkitRunnable.runTaskLater(delay: Long)
        = runTaskLater(KotlinBukkitAPI.INSTANCE, delay)

fun BukkitRunnable.runTaskLaterAsynchronously(delay: Long)
        = runTaskLaterAsynchronously(KotlinBukkitAPI.INSTANCE, delay)

fun BukkitRunnable.runTaskTimer(repeatDelay: Long, delayToStart: Long = 0)
        = runTaskTimer(KotlinBukkitAPI.INSTANCE, delayToStart, repeatDelay)

fun BukkitRunnable.runTaskTimerAsynchronously(repeatDelay: Long, delayToStart: Long = 0)
        = runTaskTimerAsynchronously(KotlinBukkitAPI.INSTANCE, delayToStart, repeatDelay)