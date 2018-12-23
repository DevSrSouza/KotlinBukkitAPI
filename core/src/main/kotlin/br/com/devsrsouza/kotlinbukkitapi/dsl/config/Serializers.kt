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
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

typealias LoadFunction<T> = (Any) -> T
typealias SaveFunction<T> = T.() -> Any

open class Serializable<T>(val default: T, val description: String = "") {

    private var _value: T = default

    internal var loadFunction: LoadFunction<T>? = null
    internal var saveFunction: SaveFunction<T>? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value
    }

    internal fun load(any: Any) = Unit.apply { if (loadFunction != null) _value = loadFunction!!.invoke(any) }
    internal fun save() = saveFunction?.invoke(_value) ?: default as Any

    fun load(block: LoadFunction<T>) {
        loadFunction = block
    }

    fun save(block: SaveFunction<T>) {
        saveFunction = block
    }
}

fun <T> serializable(default: T, description: String = "", block: Serializable<T>.() -> Unit) = Serializable(default, description).apply { block() }

fun locationSerializer(location: Location, description: String = "") = Serializable(location, description).apply {
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

fun locationListSerializer(locations: MutableList<Location>, description: String = "") = Serializable(locations, description).apply {
    load {
        (it as? List<String>)?.mapNotNull {
            it.split(";").run {
                val world = getOrNull(0)?.let { Bukkit.getWorld(it) }
                val x = getOrNull(1)?.toDoubleOrNull()
                val y = getOrNull(2)?.toDoubleOrNull()
                val z = getOrNull(3)?.toDoubleOrNull()
                val yaw = getOrNull(4)?.toFloatOrNull()
                val pitch = getOrNull(5)?.toFloatOrNull()
                if (world != null && x != null && y != null
                        && z != null) {
                    Location(world, x, y, z, yaw ?: 0F, pitch ?: 0F)
                } else null
            }
        }?.toMutableList() ?: default
    }
    save { map { "${it.world.name};${it.x};${it.y};${it.z};${it.yaw};${it.pitch}" } }
}

fun baseComponentSerializer(component: BaseComponent, description: String = "") = Serializable(component, description).apply {
    load { TextComponent(*ComponentSerializer.parse(+(it.toString()))) }
    save { -toJson().javaUnicodeToCharacter() }
}