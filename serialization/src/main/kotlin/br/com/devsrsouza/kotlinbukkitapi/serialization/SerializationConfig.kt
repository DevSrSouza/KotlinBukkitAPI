package br.com.devsrsouza.kotlinbukkitapi.serialization

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.io.File

typealias KotlinConfigEventObservable = (KotlinConfigEvent) -> Unit

enum class KotlinConfigEvent { SAVE, RELOAD }

class SerializationConfig<T : Any>(
    val defaultModel: T,
    val file: File,
    val serializer: KSerializer<T>,
    val stringFormat: StringFormat = Yaml.default,
    val eventObservable: KotlinConfigEventObservable? = null
) {
    lateinit var config: T private set

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