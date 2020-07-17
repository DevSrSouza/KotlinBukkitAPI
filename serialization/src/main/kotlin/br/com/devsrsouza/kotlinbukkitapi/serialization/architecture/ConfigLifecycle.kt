package br.com.devsrsouza.kotlinbukkitapi.serialization.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.LifecycleListener
import br.com.devsrsouza.kotlinbukkitapi.serialization.KotlinConfigEvent
import br.com.devsrsouza.kotlinbukkitapi.serialization.SerializationConfig
import br.com.devsrsouza.kotlinbukkitapi.serialization.architecture.impl.ConfigDelegate
import br.com.devsrsouza.kotlinbukkitapi.serialization.architecture.impl.getOrInsertConfigLifecycle
import br.com.devsrsouza.kotlinbukkitapi.serialization.architecture.impl.registerConfiguration
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import java.io.File
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Loads the file with the given [serializer].
 *
 * If the file not exist, one will be created with the [defaultModel] serialize into it.
 *
 * If [KotlinPlugin.reloadConfig] get called will reload the Config.
 *
 * @param file: The file name in your [dataFolder] (like config.yml).
 * @param loadOnEnable: If true, loads your configuration just when the server enable,
 * otherwise, load at the call of this function. This could be usage if your configuration
 * uses a parser that Parser a Location or a World that is not loaded yet.
 * @param saveOnDisable: If true, saves the current [SerializationConfig.model] to the configuration file.
 */
fun <T : Any> KotlinPlugin.config(
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

/**
 * Gets the config for the given [KType]
 */
fun LifecycleListener<*>.getConfig(type: KType): SerializationConfig<*> {
    try {
        val serialName = serializer(type).descriptor.serialName
        val config = plugin.getOrInsertConfigLifecycle().serializationConfigurations[serialName]

        requireNotNull(config) { "Could not find this type registred as a Config." }

        return config
    } catch (e: SerializationException) {
        throw IllegalArgumentException("The config given type is not a serializable one.")
    }
}


/**
 * Config delegate that caches the config reference.
 */

fun <T : Any> LifecycleListener<*>.config(type: KType): ConfigDelegate<T, T> {
    return config(type, { this })
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any> LifecycleListener<*>.config(): ConfigDelegate<T, T> = config<T>(typeOf<T>())

fun <T : Any, R> LifecycleListener<*>.config(
        type: KType,
        deep: T.() -> R
): ConfigDelegate<T, R> {
    return ConfigDelegate(type, deep)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any, R> LifecycleListener<*>.config(
        noinline deep: T.() -> R
): ConfigDelegate<T, R> = config(typeOf<T>(), deep)