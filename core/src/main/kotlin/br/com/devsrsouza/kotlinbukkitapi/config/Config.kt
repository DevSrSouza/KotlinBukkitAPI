package br.com.devsrsouza.kotlinbukkitapi.config

import br.com.devsrsouza.config4bukkit.HoconConfiguration
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.reflect.*
import kotlin.reflect.full.*

typealias PropertyTransformer = KProperty1<*, *>.(Any) -> Any

class BukkitConfig(val file: File, val type: ConfigurationType = ConfigurationType.YAML) : Configuration by when(type) {
    ConfigurationType.YAML -> YamlConfiguration()
    ConfigurationType.HOCON -> HoconConfiguration()
} {
    init { (this as FileConfiguration).load(file) }

    fun save() = apply { (this as FileConfiguration).save(file) }
    fun reload() = apply { (this as FileConfiguration).load(file) }
}

enum class ConfigurationType { YAML, HOCON }

fun <T : Any> ConfigurationSection.saveFrom(model: KClass<T>,
                                            instance: T = model.objectInstance ?: model.createInstance(),
                                            saveTransformer: PropertyTransformer? = null): Int {
    var change = 0
    ConfigIMPL.setTo(model, instance, saveTransformer) {
        var path = ""
        var actualEntry = it
        while (true) {
            if (actualEntry.value is Map.Entry<*, *>) {
                path += actualEntry.key + "."
                actualEntry = actualEntry.value as Map.Entry<String, Any>
            } else {
                path += actualEntry.key
                set(path, actualEntry.value)
                change++
                break
            }
        }
    }
    return change
}

fun <T : Any> ConfigurationSection.loadAndSetDefault(model: KClass<T>,
                                                     instance: T = model.objectInstance ?: model.createInstance(),
                                                     saveTransformer: PropertyTransformer? = null,
                                                     loadTransformer: PropertyTransformer? = null): Int {
    var change = 0
    ConfigIMPL.loadAndSetDefault(model, instance, toMap(), saveTransformer, loadTransformer) {
        var path = ""
        var actualEntry = it
        while (true) {
            if (actualEntry.value is Map.Entry<*, *>) {
                path += actualEntry.key + "."
                actualEntry = actualEntry.value as Map.Entry<String, Any>
            } else {
                path += actualEntry.key
                set(path, actualEntry.value)
                change++
                break
            }
        }
    }
    return change
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