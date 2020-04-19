package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.config.KotlinBukkitConfig
import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.serialization.SerializationConfig

internal fun KotlinPlugin.getOrInsertConfigLifecycle(): ConfigLifecycle {
    // MAX_VALUE = the last to execute onDisable and the first to loaded
    return getOrInsertGenericLifecycle(Int.MAX_VALUE) {
        ConfigLifecycle(this)
    }
}

internal class ConfigLifecycle(
        override val plugin: KotlinPlugin
) : LifecycleListener<KotlinPlugin> {

    internal val onEnableLoadConfigurations = mutableListOf<KotlinBukkitConfig<*>>()
    internal val onDisableSaveConfigurations = mutableListOf<KotlinBukkitConfig<*>>()

    internal val onEnableLoadSerializationConfigurations = mutableListOf<SerializationConfig<*>>()
    internal val onDisableSaveSerializationConfigurations = mutableListOf<SerializationConfig<*>>()

    override fun onPluginEnable() {
        for (config in onEnableLoadConfigurations)
            config.load()

        for (config in onEnableLoadSerializationConfigurations)
            config.load()
    }

    override fun onPluginDisable() {
        for (config in onDisableSaveConfigurations)
            config.save()

        for (config in onDisableSaveSerializationConfigurations)
            config.save()
    }
}