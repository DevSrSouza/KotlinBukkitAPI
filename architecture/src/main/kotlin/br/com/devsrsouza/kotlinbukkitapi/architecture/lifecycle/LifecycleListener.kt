package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin

/**
 * Class that listen to Lifecycle from a [KotlinPlugin]
 */
public interface LifecycleListener<T : KotlinPlugin> : PluginLifecycleListener, WithPlugin<T> {

    override fun invoke(event: LifecycleEvent) {
        when(event) {
            LifecycleEvent.LOAD -> onPluginLoad()
            LifecycleEvent.ENABLE -> onPluginEnable()
            LifecycleEvent.DISABLE -> onPluginDisable()
            LifecycleEvent.CONFIG_RELOAD -> onConfigReload()
            else -> {}
        }
    }

    /**
     * Called when the Plugin loads (before the World)
     */
    public fun onPluginLoad() {}

    /**
     * Called when the Plugin enables and is ready to register events, commands and etc...
     */
    public fun onPluginEnable() {}

    /**
     * Called when the Plugin disable like: Server Stop,
     * Reload Server or Plugins such Plugman disable the plugin.
     */
    public fun onPluginDisable() {}

    /**
     * Called when some part of your code calls [KotlinPlugin.reloadConfig]
     */
    public fun onConfigReload() {}
}