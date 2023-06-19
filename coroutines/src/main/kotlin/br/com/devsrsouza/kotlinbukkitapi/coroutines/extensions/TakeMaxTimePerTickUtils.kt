package br.com.devsrsouza.kotlinbukkitapi.coroutines.extensions

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.task
import kotlinx.coroutines.suspendCancellableCoroutine
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

public suspend fun WithPlugin<*>.takeMaxPerTick(
        time: Duration
): Unit = plugin.takeMaxPerTick(time)

public suspend fun Plugin.takeMaxPerTick(time: Duration) {
    val takeValues = getTakeValuesOrNull(coroutineContext)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(coroutineContext, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(coroutineContext)
            suspendCancellableCoroutine<Unit> { continuation ->
                val runnable = task(1) {
                    continuation.resume(Unit)
                }
                continuation.invokeOnCancellation {
                    if(runnable.isCancelled.not()) runnable.cancel()
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