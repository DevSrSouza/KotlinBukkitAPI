package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.configuration.ConfigurationSection

public fun ConfigurationSection.putAll(map: Map<String, Any?>) {
    for ((key, value) in map) {
        if (value is Map<*, *>) {
            set(key, null)
            (getConfigurationSection(key) ?: createSection(key)).putAll(value as Map<String, Any?>)
        } else {
            set(key, value)
        }
    }
}

/**
 * @returns the count of absent values that was set, if returns 0, no values was set to the Configuration
 */
public fun ConfigurationSection.putAllIfAbsent(map: Map<String, Any?>): Int {
    var missing = 0
    for ((key, value) in map) {
        if (value is Map<*, *>) {
            missing += (getConfigurationSection(key) ?: createSection(key)).putAllIfAbsent(value as Map<String, Any?>)
        } else if (!contains(key)) {
            missing++
        }
    }
    return missing
}

public fun ConfigurationSection.toMap(): Map<String, Any?> {
    return getValues(false).apply {
        forEach { k, v ->
            if (v is ConfigurationSection) {
                set(k, v.toMap())
            }
        }
    }
}
