package br.com.devsrsouza.kotlinbukkitapi.flow

import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.events
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.unregisterAllListeners
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

suspend inline fun <reified T : Event> WithPlugin<*>.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Flow<T> = plugin.eventFlow<T>(assign, priority, ignoreCancelled)

suspend inline fun <reified T : Event> Plugin.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Flow<T> = eventFlow(T::class, this, assign, priority, ignoreCancelled)

suspend fun <T : Event> eventFlow(
    type: KClass<T>,
    plugin: Plugin,
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false
): Flow<T> {
    val channel = Channel<T>(Channel.CONFLATED)

    val listener = plugin.events {}

    val flow = channel.consumeAsFlow().onStart {
        listener.event(type, priority, ignoreCancelled) {
            GlobalScope.launch {
                if (!channel.isClosedForSend && !channel.isClosedForReceive)
                    channel.send(this@event)
            }
        }
    }

    val assignListener: KListener<*>? = if (assign != null)
        plugin.events {
            fun PlayerEvent.closeChannel() {
                if (!channel.isClosedForSend && player.name == assign.name)
                    channel.close()
            }

            event<PlayerQuitEvent> { closeChannel() }
            event<PlayerKickEvent> { closeChannel() }
        }
    else null

    channel.invokeOnClose {
        listener.unregisterAllListeners()
        assignListener?.unregisterAllListeners()
    }

    return flow
}