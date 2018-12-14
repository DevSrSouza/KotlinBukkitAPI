package br.com.devsrsouza.kotlinbukkitapi.dsl.config

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.toJson
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryMinus
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.unaryPlus
import br.com.devsrsouza.kotlinbukkitapi.utils.javaUnicodeToCharacter
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

fun locationSerializer(location: Location, description: String = "")
        = Serializable(location, description).apply {
    load {
        (it as? String)?.split(";")?.run {
            val world = getOrNull(0)?.let { Bukkit.getWorld(it) }
            val x = getOrNull(1)?.toDoubleOrNull()
            val y = getOrNull(2)?.toDoubleOrNull()
            val z = getOrNull(3)?.toDoubleOrNull()
            val yaw = getOrNull(4)?.toFloatOrNull()
            val pitch = getOrNull(5)?.toFloatOrNull()
            if (world != null && x != null && y != null
                    && z != null) {
                Location(world, x, y, z,
                        yaw ?: 0F,
                        pitch ?: 0F)
            } else null
        } ?: default
    }

    save { "${world.name};$x;$y;$z;$yaw;$pitch" }
}

fun locationListSerializer(locations: MutableList<Location>, description: String = "")
        = Serializable(locations, description).apply {
    load {
        (it as? List<String>)?.mapNotNull {
            it.split(";").run {
                val world = getOrNull(0)?.let { Bukkit.getWorld(it) }
                val x = getOrNull(1)?.toDoubleOrNull()
                val y = getOrNull(2)?.toDoubleOrNull()
                val z = getOrNull(3)?.toDoubleOrNull()
                val yaw = getOrNull(4)?.toFloatOrNull()
                val pitch = getOrNull(5)?.toFloatOrNull()
                if(world != null && x != null && y != null
                        && z != null) {
                    Location(world, x, y, z, yaw ?: 0F, pitch ?: 0F)
                } else null
            }
        }?.toMutableList() ?: default
    }
    save { map { "${it.world.name};${it.x};${it.y};${it.z};${it.yaw};${it.pitch}" } }
}

fun baseComponentSerializer(component: BaseComponent, description: String = "")
        = Serializable(component, description).apply {
    load { TextComponent(*ComponentSerializer.parse(+(it.toString()))) }
    save { -toJson().javaUnicodeToCharacter() }
}