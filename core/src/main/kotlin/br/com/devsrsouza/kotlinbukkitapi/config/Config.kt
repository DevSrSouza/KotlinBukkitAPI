package br.com.devsrsouza.kotlinbukkitapi.config

import br.com.devsrsouza.json4bukkit.JsonConfiguration
import br.com.devsrsouza.kotlinbukkitapi.extensions.putAll
import br.com.devsrsouza.kotlinbukkitapi.extensions.putAllIfAbsent
import br.com.devsrsouza.kotlinbukkitapi.extensions.toMap
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.*

class BukkitConfig(
        val file: File,
        private val fileConfiguration: FileConfiguration
) : Configuration by fileConfiguration {

    constructor(file: File, type: ConfigurationType = ConfigurationType.YAML) : this(file, when (type) {
        ConfigurationType.YAML -> YamlConfiguration()
        ConfigurationType.JSON -> JsonConfiguration()
    })

    init { fileConfiguration.load(file) }

    fun save() = apply { fileConfiguration.save(file) }
    fun reload() = apply { fileConfiguration.load(file) }
}

enum class ConfigurationType { YAML, JSON }

/**
 * Save all public `var` property from the [instance] into the config.
 */
fun <T : Any> ConfigurationSection.saveFrom(
        instance: T,
        adapter: PropertySaveAdapter = defaultSaveAdapter()
) {
    val serialized = KotlinSerializer.instanceToMap(instance, adapter)

    putAll(serialized)
}

/**
 * Save all missing public `var` property values from [instance] into the config.
 */
fun <T : Any> ConfigurationSection.saveMissingFrom(
        instance: T,
        adapter: PropertySaveAdapter = defaultSaveAdapter()
): Int {
    val serialized = KotlinSerializer.instanceToMap(instance, adapter)

    return putAllIfAbsent(serialized)
}

/**
 * Load from the config the values based on public `var` property from the [type].
 */
fun <T : Any> ConfigurationSection.loadFrom(
        type: KClass<T>,
        adapter: PropertyLoadAdapter = defaultLoadAdapter()
) {
    val map = toMap()
    KotlinSerializer.mapToInstance(type, map, adapter)
}
