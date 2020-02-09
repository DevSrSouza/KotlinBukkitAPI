package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.config.KotlinBukkitConfig
import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin

internal fun KotlinPlugin.getOrInsertConfigLifecycle(): ConfigLifecycle {
    // MAX_VALUE = the last to execute onDisable
    return getOrInsertGenericLifecycle(Int.MAX_VALUE) {
        ConfigLifecycle(this)
    }
}

internal class ConfigLifecycle(
        override val plugin: KotlinPlugin
) : LifecycleListener<KotlinPlugin> {

    internal val onEnableLoadConfigurations = mutableListOf<KotlinBukkitConfig<*>>()
    internal val onDisableSaveConfigurations = mutableListOf<KotlinBukkitConfig<*>>()

    override fun onPluginEnable() {
        for (config in onEnableLoadConfigurations)
            config.load()
    }

    override fun onPluginDisable() {
        for (config in onDisableSaveConfigurations)
            config.save()
    }
}