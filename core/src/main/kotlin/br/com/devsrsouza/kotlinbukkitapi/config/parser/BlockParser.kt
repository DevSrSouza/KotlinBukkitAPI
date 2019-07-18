package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import java.lang.IllegalArgumentException

class BlockParser(
        val type: ParserType = ParserType.MAP
) : ObjectParser<Block> {
    override fun parse(any: Any): Block {
        if(any is String) {
            var slices = any.split(";")

            val world = Bukkit.getWorld(slices[0])
            val x = slices[1].toInt()
            val y = slices[2].toInt()
            val z = slices[3].toInt()

            return world.getBlockAt(x, y, z)
        } else if(any is BlockWrapper) {
            return any.toBlock()
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Location")
    }

    override fun render(element: Block): Any {
        element.run {
            return when (this@BlockParser.type) {
                ParserType.STRING -> "${world.name};$x;$y;$z"
                ParserType.MAP -> BlockWrapper(world.name, x, y, z)
            }
        }
    }
}

data class BlockWrapper(
        var world: String = "world",
        var x: Int = 0,
        var y: Int = 64,
        var z: Int = 0
) {
    fun toBlock() = Bukkit.getWorld(world).getBlockAt(x, y, z)
}