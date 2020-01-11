package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import br.com.devsrsouza.kotlinbukkitapi.utils.time.Millisecond
import com.okkero.skedule.BukkitSchedulerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal val coroutineContextTakes = ConcurrentHashMap<CoroutineContext, TakeValues>()
internal data class TakeValues(val startTimeMilliseconds: Long, val takeTimeMillisecond: Long) {
    fun wasTimeExceeded() = System.currentTimeMillis() - startTimeMilliseconds - takeTimeMillisecond >= 0
}

suspend fun Plugin.takeMaxPerTick(time: Millisecond) {
    val takeValues = getTakeValuesOrNull(coroutineContext)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(coroutineContext, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(coroutineContext)
            withContext(BukkitDispatchers.SYNC) {
                // wait next tick using BukkitScheduler
                delay(20)
            }
        }
    }
}

internal fun getTakeValuesOrNull(
        coroutineContext: CoroutineContext
): TakeValues? = coroutineContextTakes[coroutineContext]

internal fun registerCoroutineContextTakes(
        coroutineContext: CoroutineContext,
        time: Millisecond
) {
    coroutineContextTakes.put(
            coroutineContext,
            TakeValues(System.currentTimeMillis(), time.toMillisecond())
    )
}

internal fun unregisterCoroutineContextTakes(
        coroutineContext: CoroutineContext
) {
    coroutineContextTakes.remove(coroutineContext)
}