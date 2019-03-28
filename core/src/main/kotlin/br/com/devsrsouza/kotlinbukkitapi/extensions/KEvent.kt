package br.com.devsrsouza.kotlinbukkitapi.extensions.event

import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import kotlin.reflect.full.safeCast

inline fun <reified T : Event> KListener.event(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = true,
        crossinline block: T.() -> Unit
) = event(plugin, priority, ignoreCancelled, block)

inline fun <reified T : Event> Listener.event(
        plugin: Plugin,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = true,
        crossinline block: T.() -> Unit
) {
    Bukkit.getServer().pluginManager.registerEvent(
            T::class.java,
            this,
            priority,
            { _, event ->
                T::class.safeCast(event)?.block()
            },
            plugin,
            ignoreCancelled
    )
}

inline fun Plugin.events(block: KListener.() -> Unit) = InlineKListener(this).apply(block)

fun Listener.unregisterAll() = HandlerList.unregisterAll(this)

fun Listener.registerEvents(plugin: Plugin)
        = plugin.server.pluginManager.registerEvents(this, plugin)

fun Event.callEvent() = Bukkit.getServer().pluginManager.callEvent(this)

val PlayerMoveEvent.displaced: Boolean
    get() = this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z

interface KListener : Listener { val plugin: Plugin }
inline class InlineKListener(override val plugin: Plugin) : KListener