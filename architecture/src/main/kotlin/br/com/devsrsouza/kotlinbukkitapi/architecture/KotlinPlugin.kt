package br.com.devsrsouza.kotlinbukkitapi.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.Lifecycle
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.LifecycleListener
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.getOrInsertConfigLifecycle
import br.com.devsrsouza.kotlinbukkitapi.config.ConfigurationType
import br.com.devsrsouza.kotlinbukkitapi.config.KotlinBukkitConfig
import br.com.devsrsouza.kotlinbukkitapi.config.KotlinConfigEvent
import br.com.devsrsouza.kotlinbukkitapi.serialization.SerializationConfig
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ConcurrentSkipListSet

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
     */
    fun registerKotlinPluginLifecycle(
            priority: Int = 1,
            listener: LifecycleListener<KotlinPlugin>
    ) {
        lifecycleListeners.add(Lifecycle(priority, listener))
    }

    /**
     * Load and save missing values from a [model] to a configuration file with the
     * name of the [file] parameter.
     *
     * @param file: The file name in your [dataFolder] (like config.yml).
     * @param loadOnEnable: If true, loads your configuration just when the server enable,
     * otherwise, load at the call of this function. This could be usage if your configuration
     * uses a parser that Parser a Location or a World that is not loaded yet.
     * @param saveOnDisable: If true, saves the [model] to the configuration file.
     */
    fun <T : Any> config(
            file: String,
            model: T,
            type: ConfigurationType = ConfigurationType.YAML,
            loadOnEnable: Boolean = false,
            saveOnDisable: Boolean = false
    ): KotlinBukkitConfig<T> {
        val configFile = File(dataFolder, file)

        return KotlinBukkitConfig(model, configFile, type, eventObservable = {
            if (it == KotlinConfigEvent.RELOAD)
                someConfigReloaded()
        }).also {
            registerConfiguration(it as KotlinBukkitConfig<Any>, loadOnEnable, saveOnDisable)
        }
    }

    /**
     * Loads the file with the given [serializer].
     *
     * If the file not exist, one will be created with the [defaultModel] serialize into it.
     *
     * @param file: The file name in your [dataFolder] (like config.yml).
     * @param loadOnEnable: If true, loads your configuration just when the server enable,
     * otherwise, load at the call of this function. This could be usage if your configuration
     * uses a parser that Parser a Location or a World that is not loaded yet.
     * @param saveOnDisable: If true, saves the current [SerializationConfig.model] to the configuration file.
     */
    fun <T : Any> config(
        file: String,
        defaultModel: T,
        serializer: KSerializer<T>,
        type: StringFormat = Yaml.default,
        loadOnEnable: Boolean = false,
        saveOnDisable: Boolean = false
    ): SerializationConfig<T> {
        val configFile = File(dataFolder, file)

        return SerializationConfig(
            defaultModel,
            configFile,
            serializer,
            type,
            eventObservable = {
                if (it == KotlinConfigEvent.RELOAD)
                    someConfigReloaded()
            }
        ).also {
            registerConfiguration(it as SerializationConfig<Any>, loadOnEnable, saveOnDisable)
        }
    }

    // implementation stuff, ignore...

    internal val lifecycleListeners = ConcurrentSkipListSet<Lifecycle>()
    internal val configurations = mutableListOf<KotlinBukkitConfig<Any>>()
    internal val serializationConfigurations = mutableListOf<SerializationConfig<Any>>()

    final override fun onLoad() {
        onPluginLoad()

        for(lifecycle in lifecycleListeners)
            lifecycle.listener.onPluginLoad()
    }

    final override fun onEnable() {
        onPluginEnable()

        for(lifecycle in lifecycleListeners)
            lifecycle.listener.onPluginEnable()
    }

    final override fun onDisable() {
        onPluginDisable()

        // reversing lifecycles for execute first the low priority ones
        val reversedLifecyle = lifecycleListeners.descendingSet()

        for(lifecycle in reversedLifecyle)
            lifecycle.listener.onPluginDisable()
    }

    final override fun reloadConfig() {
        super.reloadConfig()

        for (config in configurations)
            config.reload()

        for (config in serializationConfigurations)
            config.reload()

        someConfigReloaded()
    }

    private fun someConfigReloaded() {
        onConfigReload()

        for(lifecycle in lifecycleListeners)
            lifecycle.listener.onConfigReload()
    }

    private fun registerConfiguration(
            config: KotlinBukkitConfig<Any>,
            loadOnEnable: Boolean,
            saveOnDisable: Boolean
    ) {
        configurations.add(config)

        if(loadOnEnable) {
            val configLifecycle = getOrInsertConfigLifecycle()
            configLifecycle.onEnableLoadConfigurations.add(config)
        } else {
            config.load()
        }

        if(saveOnDisable) {
            val configLifecycle = getOrInsertConfigLifecycle()
            configLifecycle.onDisableSaveConfigurations.add(config)
        }
    }

    private fun registerConfiguration(
        config: SerializationConfig<Any>,
        loadOnEnable: Boolean,
        saveOnDisable: Boolean
    ) {
        serializationConfigurations.add(config)

        if(loadOnEnable) {
            val configLifecycle = getOrInsertConfigLifecycle()
            configLifecycle.onEnableLoadSerializationConfigurations.add(config)
        } else {
            config.load()
        }

        if(saveOnDisable) {
            val configLifecycle = getOrInsertConfigLifecycle()
            configLifecycle.onDisableSaveSerializationConfigurations.add(config)
        }
    }

}