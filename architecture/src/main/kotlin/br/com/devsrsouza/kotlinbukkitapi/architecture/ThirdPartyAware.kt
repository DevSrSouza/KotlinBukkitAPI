package br.com.devsrsouza.kotlinbukkitapi.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.util.WeakHashMap

/**
 * A holder for all instance of a class [T] that is bound a specific plugin.
 * It unregister when the plugin is disable.
 */
public abstract class PluginDisableAwareController<T : PluginDisableAware> {

    public abstract val factory: (plugin: Plugin) -> T

    private val instances: WeakHashMap<Plugin, T> = WeakHashMap()
    private val listeners: WeakHashMap<Plugin, Listener> = WeakHashMap()

    public fun get(plugin: Plugin): T {
        return instances.getOrPut(plugin) {
            listeners.put(
                plugin,
                DisableListener(plugin, ::disable).apply {
                    Bukkit.getServer().pluginManager.registerEvents(this, plugin)
                },
            )
            factory(plugin)
        }
    }

    public fun ensureInitialized(plugin: Plugin) {
        get(plugin)
    }

    private fun disable(plugin: Plugin) {
        instances.remove(plugin)?.onDisable()
        listeners.remove(plugin)?.also(HandlerList::unregisterAll)
    }
}

public interface PluginDisableAware : WithPlugin<Plugin> {
    public fun onDisable()
}

private class DisableListener(
    val plugin: Plugin,
    val onDisable: (Plugin) -> Unit,
) : Listener {
    @EventHandler
    public fun pluginDisableEvent(event: PluginDisableEvent) {
        if (event.plugin.name == plugin.name) {
            onDisable(plugin)
        }
    }
}
