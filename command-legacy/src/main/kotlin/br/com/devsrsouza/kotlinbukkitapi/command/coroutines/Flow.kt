package br.com.devsrsouza.kotlinbukkitapi.command.coroutines

import br.com.devsrsouza.kotlinbukkitapi.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.coroutines.flow.playerEventFlow
import br.com.devsrsouza.kotlinbukkitapi.extensions.SimpleKListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent

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
public inline fun <reified T : PlayerEvent> Executor<Player>.playerEventFlow(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel<T>(Channel.CONFLATED),
    listener: Listener = SimpleKListener(command.plugin),
): Flow<T> = playerEventFlow(sender, command.plugin, priority, ignoreCancelled, channel, listener)