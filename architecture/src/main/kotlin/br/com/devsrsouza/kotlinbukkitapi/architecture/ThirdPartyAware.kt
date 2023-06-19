package br.com.devsrsouza.kotlinbukkitapi.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.util.WeakHashMap

/**
 * A holder for all instance of a class [T] that is bound a specific plugin.
 * It unregister when the plugin is disable.
 * TODO: plugin disable support
 */
public abstract class PluginDisableAwareController<T : PluginDisableAware> {

    public abstract val factory: (plugin: Plugin) -> T

    private val instances: WeakHashMap<Plugin, T> = WeakHashMap()

    public fun get(plugin: Plugin): T {
        return instances.getOrPut(plugin) { factory(plugin) }
    }

    public fun ensureInitialized(plugin: Plugin) {
        get(plugin)
    }

    @EventHandler
    public fun pluginDisableEvent(event: PluginDisableEvent) {
        // todo:
    }
}

public interface PluginDisableAware : WithPlugin<Plugin> {
    public fun onDisable()
}
