package br.com.devsrsouza.kotlinbukkitapi.flow

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.SimpleKListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.Plugin

/**
 * Creates a event flow for any `PlayerEvent` that auto filters for the player that send the command.
 *
 * Use case:
 * ```kotlin
 * executorPlayer {
 *    sender.msg("Plz, send your faction description in the chat")
 *
 *    val description = commandPlayerEventFlow<AsyncPlayerChatEvent>()
 *                  .first()
 *                  .message
 *
 *    faction.description = description
 *
 *   sender.msg("You set the faction description to: $description")
 * }
 * ```
 */
inline fun <reified T : PlayerEvent> Executor<Player>.commandPlayerEventFlow(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel<T>(Channel.CONFLATED),
        listener: Listener = SimpleKListener(command.plugin),
) = playerEventFlow(sender, command.plugin, priority, ignoreCancelled, channel, listener)

/**
 * Creates a event flow for [PlayerEvent] that auto filter for only events from [player].
 */
inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
        player: Player,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel<T>(Channel.CONFLATED),
        listener: Listener = SimpleKListener(plugin),
) = plugin.playerEventFlow(player, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> Plugin.playerEventFlow(
        player: Player,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel<T>(Channel.CONFLATED),
        listener: Listener = SimpleKListener(this),
) = playerEventFlow(player, this, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> playerEventFlow(
        player: Player,
        plugin: Plugin,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel<T>(Channel.CONFLATED),
        listener: Listener = SimpleKListener(plugin),
) = eventFlow<T>(T::class, plugin, player, priority, ignoreCancelled, channel, listener)
        .filter { it.player.name == player.name }