package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.task
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration

internal val coroutineContextTakes = ConcurrentHashMap<CoroutineContext, TakeValues>()
internal data class TakeValues(val startTimeMilliseconds: Long, val takeTimeMillisecond: Long) {
    fun wasTimeExceeded() = System.currentTimeMillis() - startTimeMilliseconds - takeTimeMillisecond >= 0
}

suspend fun WithPlugin<*>.takeMaxPerTick(
        time: Duration
) = plugin.takeMaxPerTick(time)

suspend fun Plugin.takeMaxPerTick(time: Duration) {
    val takeValues = getTakeValuesOrNull(coroutineContext)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(coroutineContext, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(coroutineContext)
            suspendCoroutine<Unit> { continuation ->
                task(1) {
                    continuation.resume(Unit)
                }
            }
        }
    }
}

internal fun getTakeValuesOrNull(
        coroutineContext: CoroutineContext
): TakeValues? = coroutineContextTakes[coroutineContext]

internal fun registerCoroutineContextTakes(
        coroutineContext: CoroutineContext,
        time: Duration
) {
    coroutineContextTakes.put(
            coroutineContext,
            TakeValues(System.currentTimeMillis(), time.inWholeMilliseconds)
    )
}

internal fun unregisterCoroutineContextTakes(
        coroutineContext: CoroutineContext
) {
    coroutineContextTakes.remove(coroutineContext)
}