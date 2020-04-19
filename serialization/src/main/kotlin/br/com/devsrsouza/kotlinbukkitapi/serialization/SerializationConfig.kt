package br.com.devsrsouza.kotlinbukkitapi.serialization

import br.com.devsrsouza.kotlinbukkitapi.config.KotlinConfigEvent
import br.com.devsrsouza.kotlinbukkitapi.config.KotlinConfigEventObservable
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.io.File

class SerializationConfig<T : Any>(
    var model: T,
    val file: File,
    val serializer: KSerializer<T>,
    val stringFormat: StringFormat = Yaml.default,
    val eventObservable: KotlinConfigEventObservable? = null
) {

    fun load() {
        createFileIfNotExist()

        loadFromFile()
    }

    /**
     * Save the current values of [model] in the configuration file.
     */
    fun save(): SerializationConfig<T> = apply {
        saveToFile()

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
        model = stringFormat.parse(serializer, file.readText())
    }

    private fun stringifyModel() = stringFormat.stringify(serializer, model)

    private fun saveToFile() {
        val content = stringifyModel()
        file.writeText(content)
    }

    private fun createFileIfNotExist() {
        if(!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()

            saveToFile()
        }
    }
}