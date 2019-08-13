package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType

class LocationParser(
        val type: ParserType = ParserType.MAP
) : ObjectParser<Location> {
    override fun parse(any: Any): Location {
        if(any is String) {
            var slices = any.split(";")

            val world = Bukkit.getWorld(slices[0])
            val x = slices[1].toDouble()
            val y = slices[2].toDouble()
            val z = slices[3].toDouble()
            val yaw = slices[4].toFloat()
            val pitch = slices[5].toFloat()

            return Location(world, x, y, z, yaw, pitch)
        } else if(any is Map<*, *>) {
            val map = any as Map<String, Any>
            val world = Bukkit.getWorld(map["world"] as String)
            val x = (map["x"] as Number).toDouble()
            val y = (map["y"] as Number).toDouble()
            val z = (map["z"] as Number).toDouble()
            val yaw = (map["yaw"] as Number).toFloat()
            val pitch = (map["pitch"] as Number).toFloat()
            return Location(world, x, y, z, yaw, pitch)
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Location")
    }

    override fun render(element: Location): Pair<Any, KType> {
        element.run {
            return when (type) {
                ParserType.STRING -> "${world.name};$x;$y;$z;$yaw;$pitch" to String::class.createType()
                ParserType.MAP -> LocationWrapper(world.name, x, y, z, yaw, pitch) to LocationWrapper::class.createType()
            }
        }
    }
}

data class LocationWrapper(
        var world: String = "world",
        var x: Double = 0.0,
        var y: Double = 64.0,
        var z: Double = 0.0,
        var yaw: Float = 0f,
        var pitch: Float = 0f
) {
    fun toLocation() = Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)
}