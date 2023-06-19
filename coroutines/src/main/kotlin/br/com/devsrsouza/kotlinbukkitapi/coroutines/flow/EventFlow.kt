package br.com.devsrsouza.kotlinbukkitapi.coroutines.flow

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.SimpleKListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.events
import br.com.devsrsouza.kotlinbukkitapi.extensions.unregisterListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

public inline fun <reified T : Event> WithPlugin<*>.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = plugin.events {},
    assignListener: Listener = plugin.events {}
): Flow<T> = plugin.eventFlow<T>(assign, priority, ignoreCancelled, channel, listener, assignListener)

public inline fun <reified T : Event> Plugin.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = events {},
    assignListener: Listener = events {}
): Flow<T> = eventFlow(T::class, this, assign, priority, ignoreCancelled, channel, listener, assignListener)

/**
 * Create a Event Flow that receives the specified Event [type].
 *
 * [assign] is use for auto cancel the Flow when the Player disconnects.
 */
public fun <T : Event> eventFlow(
    type: KClass<T>,
    plugin: Plugin,
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
    assignListener: Listener = SimpleKListener(plugin)
): Flow<T> {

    val flow = channel.consumeAsFlow().onStart {
        listener.event(plugin, type, priority, ignoreCancelled) {
            GlobalScope.launch {
                if (!channel.isClosedForSend && !channel.isClosedForReceive)
                    channel.send(this@event)
            }
        }
    }

    val assignListener: Listener? = if (assign != null)
        assignListener.apply {
            fun PlayerEvent.closeChannel() {
                if (!channel.isClosedForSend && player.name == assign.name)
                    channel.close()
            }

            event<PlayerQuitEvent>(plugin) { closeChannel() }
            event<PlayerKickEvent>(plugin) { closeChannel() }
        }
    else null

    channel.invokeOnClose {
        listener.unregisterListener()
        assignListener?.unregisterListener()
    }

    return flow
}