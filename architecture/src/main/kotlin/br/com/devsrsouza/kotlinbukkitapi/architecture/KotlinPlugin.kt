package br.com.devsrsouza.kotlinbukkitapi.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.*
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

// remove all this APIs and create a `lifecycle aware` implementation.
// This will be possible to integrate the lifecycle mechanism of KBAPI in non Bukkit plugins
// for example in Nova Addon
// more close to what LifecycleOwner is in the Android API
// this could be accomplished using delegate implementation

public abstract class KotlinPlugin : JavaPlugin() {

    public open fun onPluginLoad() {}
    public open fun onPluginEnable() {}
    public open fun onPluginDisable() {}
    public open fun onConfigReload() {}

    /**
     * Register the lifecycle listener returned by the [block] lambda with the given [priority].
     *
     * **Priority Order**: High priority loads first and disable lastly
     */
    public inline fun <P : KotlinPlugin, T : LifecycleListener<P>> lifecycle(
            priority: Int = 1,
            block: () -> T
    ): T = block().also {
        registerKotlinPluginLifecycle(priority, it as LifecycleListener<KotlinPlugin>)
    }

    /**
     * Register a lifecycle listener with the given [priority].
     *
     * **Priority Order**: High priority loads first and disable lastly
     */
    public fun registerKotlinPluginLifecycle(
            priority: Int = 1,
            listener: PluginLifecycleListener
    ) {
        _lifecycleListeners.put(
                Lifecycle(
                        priority,
                        listener
                ),
                true
        )
    }

    // implementation stuff, ignore...

    private val _lifecycleListeners = ConcurrentHashMap<Lifecycle, Boolean>()
    public val lifecycleListeners: Set<Lifecycle> = _lifecycleListeners.keys

    private fun lifecycleLoadOrder() = _lifecycleListeners.keys.sortedBy { it.priority }.asReversed()
    private fun lifecycleDisableOrder() = _lifecycleListeners.keys.sortedBy { it.priority }

    final override fun onLoad() {
        onPluginLoad()

        for(lifecycle in lifecycleLoadOrder())
            lifecycle.listener(LifecycleEvent.LOAD)
    }

    final override fun onEnable() {
        onPluginEnable()

        for(lifecycle in lifecycleLoadOrder())
            lifecycle.listener(LifecycleEvent.ENABLE)
    }

    final override fun onDisable() {
        onPluginDisable()

        // reversing lifecycles for execute first the low priority ones
        val reversedLifecyle = lifecycleDisableOrder()

        for(lifecycle in reversedLifecyle)
            lifecycle.listener(LifecycleEvent.DISABLE)
    }

    final override fun reloadConfig() {
        super.reloadConfig()

        someConfigReloaded(all = true)
    }

    public fun someConfigReloaded(all: Boolean = false) {
        onConfigReload()

        for(lifecycle in lifecycleListeners)
            lifecycle.listener(
                    if(all)
                        LifecycleEvent.ALL_CONFIG_RELOAD
                                else LifecycleEvent.CONFIG_RELOAD
            )
    }

}