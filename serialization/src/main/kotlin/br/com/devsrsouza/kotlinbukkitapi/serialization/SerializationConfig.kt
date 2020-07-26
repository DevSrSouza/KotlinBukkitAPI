package br.com.devsrsouza.kotlinbukkitapi.serialization

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.bukkit.BukkitSerializationDecodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.bukkit.BukkitSerializationEncodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.StringFormatInterceptor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.io.File

typealias KotlinConfigEventObservable = (KotlinConfigEvent) -> Unit

enum class KotlinConfigEvent { SAVE, RELOAD }

/**
 * A helper class to work with Kotlinx.serialization for Bukkit plugins config.
 *
 * Additional features: Using a interceptor we are able to add new annotations to the Kotlinx.serialization.
 *
 * `@ChangeColor` in a String property translate the color codes from the Configuration, saves in `&` and loads in ``.
 */
class SerializationConfig<T : Any>(
    val defaultModel: T,
    val file: File,
    val serializer: KSerializer<T>,
    stringFormat: StringFormat,
    val alwaysRestoreDefaults: Boolean,
    val eventObservable: KotlinConfigEventObservable? = null
) {
    lateinit var config: T private set

    val stringFormat = StringFormatInterceptor(
            stringFormat,
            BukkitSerializationEncodeInterceptor,
            BukkitSerializationDecodeInterceptor
    )

    fun load() {
        createFileIfNotExist()

        loadFromFile()
    }

    /**
     * Save the current values of [model] in the configuration file.
     */
    fun save(): SerializationConfig<T> = apply {
        saveToFile(config)

        eventObservable?.invoke(KotlinConfigEvent.SAVE)
    }

    /**
     * Reloads the current values from the configuration to the [model].
     */
    fun reload(): SerializationConfig<T> = apply {
        loadFromFile()

        eventObservable?.invoke(KotlinConfigEvent.RELOAD)
    }

    private fun loadFromFile() {
        config = stringFormat.parse(serializer, file.readText())

        if(alwaysRestoreDefaults)
            saveToFile(config)
    }

    private fun stringifyModel(value: T) = stringFormat.stringify(serializer, value)

    private fun saveToFile(value: T) {
        val content = stringifyModel(value)
        file.writeText(content)
    }

    private fun createFileIfNotExist() {
        if(!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()

            saveToFile(defaultModel)
        }
    }
}