package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.Chunk
import java.lang.IllegalArgumentException

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
        } else if(any is ChunkWrapper) {
            return any.toChunk()
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Chunk")
    }

    override fun render(element: Chunk): Any {
        element.run {
            return when (type) {
                ParserType.STRING -> "${world.name};$x;$z"
                ParserType.MAP -> ChunkWrapper(world.name, x, z)
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