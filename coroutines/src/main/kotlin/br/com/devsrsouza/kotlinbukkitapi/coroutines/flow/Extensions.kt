package br.com.devsrsouza.kotlinbukkitapi.coroutines.flow

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.SimpleKListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.Plugin

/**
 * Creates a event flow for [PlayerEvent] that auto filter for only events from [player].
 */
public inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
): Flow<T> = plugin.playerEventFlow(player, priority, ignoreCancelled, channel, listener)

public inline fun <reified T : PlayerEvent> Plugin.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this),
): Flow<T> = playerEventFlow(player, this, priority, ignoreCancelled, channel, listener)

public inline fun <reified T : PlayerEvent> playerEventFlow(
    player: Player,
    plugin: Plugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
): Flow<T> = eventFlow<T>(T::class, plugin, player, priority, ignoreCancelled, channel, listener)
    .filter { it.player.name == player.name }
