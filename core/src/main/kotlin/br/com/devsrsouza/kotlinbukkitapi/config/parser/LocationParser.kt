package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.IllegalArgumentException

enum class LocationParserType { STRING, MAP }

class LocationParser(
        val type: LocationParserType = LocationParserType.MAP
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
        } else if(any is LocationWrapper) {
            return any.toLocation()
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Location")
    }

    override fun render(element: Location): Any {
        element.run {
            return when (type) {
                LocationParserType.STRING -> "${world.name};$x;$y;$z;$yaw;$pitch"
                LocationParserType.MAP -> LocationWrapper(world.name, x, y, z, yaw, pitch)
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