package br.com.devsrsouza.kotlinbukkitapi.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.*
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

open class KotlinPlugin : JavaPlugin() {

    open fun onPluginLoad() {}
    open fun onPluginEnable() {}
    open fun onPluginDisable() {}
    open fun onConfigReload() {}

    /**
     * Register the lifecycle listener returned by the [block] lambda with the given [priority].
     *
     * **Priority Order**: High priority loads first and disable lastly
     */
    inline fun <P : KotlinPlugin, T : LifecycleListener<P>> lifecycle(
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
    fun registerKotlinPluginLifecycle(
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
    val lifecycleListeners: Set<Lifecycle> = _lifecycleListeners.keys

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

    fun someConfigReloaded(all: Boolean = false) {
        onConfigReload()

        for(lifecycle in lifecycleListeners)
            lifecycle.listener(
                    if(all)
                        LifecycleEvent.ALL_CONFIG_RELOAD
                                else LifecycleEvent.CONFIG_RELOAD
            )
    }

}