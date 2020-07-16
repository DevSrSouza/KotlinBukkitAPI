package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin

/**
 * Class that listen to Lifecycle from a [KotlinPlugin]
 */
interface LifecycleListener<T : KotlinPlugin> : PluginLifecycleListener, WithPlugin<T> {

    override fun invoke(event: LifecycleEvent) {
        when(event) {
            LifecycleEvent.LOAD -> onPluginLoad()
            LifecycleEvent.ENABLE -> onPluginEnable()
            LifecycleEvent.DISABLE -> onPluginDisable()
            LifecycleEvent.CONFIG_RELOAD -> onConfigReload()
        }
    }

    /**
     * Called when the Plugin loads (before the World)
     */
    fun onPluginLoad() {}

    /**
     * Called when the Plugin enables and is ready to register events, commands and etc...
     */
    fun onPluginEnable() {}

    /**
     * Called when the Plugin disable like: Server Stop,
     * Reload Server or Plugins such Plugman disable the plugin.
     */
    fun onPluginDisable() {}

    /**
     * Called when some part of your code calls [KotlinPlugin.reloadConfig]
     */
    fun onConfigReload() {}
}