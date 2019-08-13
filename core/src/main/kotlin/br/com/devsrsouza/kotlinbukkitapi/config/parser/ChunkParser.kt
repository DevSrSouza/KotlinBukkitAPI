package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.Chunk
import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType

class ChunkParser(
        val type: ParserType = ParserType.MAP
) : ObjectParser<Chunk> {
    override fun parse(any: Any): Chunk {
        if(any is String) {
            var slices = any.split(";")

            val world = Bukkit.getWorld(slices[0])
            val x = slices[1].toInt()
            val z = slices[3].toInt()

            return world.getChunkAt(x, z)
        } else if(any is Map<*, *>) {
            val map = any as Map<String, Any>
            val world = Bukkit.getWorld(map["world"] as String)
            val x = map["x"] as Int
            val z = map["z"] as Int
            return world.getChunkAt(x, z)
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Chunk")
    }

    override fun render(element: Chunk): Pair<Any, KType> {
        element.run {
            return when (type) {
                ParserType.STRING -> "${world.name};$x;$z" to String::class.createType()
                ParserType.MAP -> ChunkWrapper(world.name, x, z) to ChunkWrapper::class.createType()
            }
        }
    }
}

data class ChunkWrapper(
        var world: String = "world",
        var x: Int = 0,
        var z: Int = 0
) {
    fun toChunk() = Bukkit.getWorld(world).getChunkAt(x, z)
}