package br.com.devsrsouza.kotlinbukkitapi.config

import java.io.File

typealias KotlinConfigEventObservable = (KotlinConfigEvent) -> Unit

enum class KotlinConfigEvent { SAVE, RELOAD }

class KotlinBukkitConfig<T : Any>(
        val model: T,
        val file: File,
        val type: ConfigurationType = ConfigurationType.YAML,
        val eventObservable: KotlinConfigEventObservable? = null
) {
    private val bukkitConfig = BukkitConfig(file, type)

    fun load() {
        file.parentFile.mkdir()
        if(!file.exists()) file.createNewFile()

        if(bukkitConfig.saveMissingFrom(model) > 0)
            save()

        loadFromModel()
    }

    /**
     * Save the current values of [model] in the configuration file.
     */
    fun save(): KotlinBukkitConfig<T> = apply {
        bukkitConfig.saveFrom(model)

        bukkitConfig.save()

        eventObservable?.invoke(KotlinConfigEvent.SAVE)
    }

    /**
     * Reloads the current values from the configuration to the [model].
     */
    fun reload(): KotlinBukkitConfig<T> = apply {
        bukkitConfig.reload()

        loadFromModel()

        eventObservable?.invoke(KotlinConfigEvent.RELOAD)
    }

    private fun loadFromModel() {
        bukkitConfig.loadFrom(model::class)
    }
}