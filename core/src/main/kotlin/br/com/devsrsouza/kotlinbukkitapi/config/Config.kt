package br.com.devsrsouza.kotlinbukkitapi.config

import br.com.devsrsouza.config4bukkit.HoconConfiguration
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.*
import kotlin.reflect.full.*

class BukkitConfig(
        val file: File,
        private val fileConfiguration: FileConfiguration
) : Configuration by fileConfiguration {

    constructor(file: File, type: ConfigurationType = ConfigurationType.YAML) : this(file, when (type) {
        ConfigurationType.YAML -> YamlConfiguration()
        ConfigurationType.HOCON -> HoconConfiguration()
    })

    init { fileConfiguration.load(file) }

    fun save() = apply { fileConfiguration.save(file) }
    fun reload() = apply { fileConfiguration.load(file) }
}

enum class ConfigurationType { YAML, HOCON }

fun <T : Any> ConfigurationSection.saveFrom(
        instance: T,
        adapter: PropertyAdapter = defaultSaveAdapter()
) {
    val serialized = KotlinSerializer.instanceToMap(instance, adapter)

    putAll(serialized)
}

fun <T : Any> ConfigurationSection.loadFrom(
        type: KClass<T>,
        adapter: PropertyAdapter = defaultLoadAdapter()
) {
    val map = toMap()
    KotlinSerializer.mapToInstance(type, map, adapter)
}

fun ConfigurationSection.putAll(map: Map<String, Any>) {
    for ((key, value) in map) {
        if(value is Map<*, *>) {
            (getConfigurationSection(key) ?: createSection(key)).putAll(value as Map<String, Any>)
        } else set(key, value)
    }
}

fun ConfigurationSection.toMap(): Map<String, Any> {
    return getValues(true).apply {
        forEach { k, v ->
            if (v is ConfigurationSection) {
                set(k, v.toMap())
            }
        }
    }
}