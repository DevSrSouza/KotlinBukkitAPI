package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Bukkit
import org.bukkit.block.Block
import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.full.createType

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
        } else if(any is Map<*, *>) {
            val map = any as Map<String, Any>
            val world = Bukkit.getWorld(map["world"] as String)
            val x = map["x"] as Int
            val y = map["y"] as Int
            val z = map["z"] as Int
            return world.getBlockAt(x, y, z)
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to Location")
    }

    override fun render(element: Block): Pair<Any, KType> {
        element.run {
            return when (this@BlockParser.type) {
                ParserType.STRING -> "${world.name};$x;$y;$z" to String::class.createType()
                ParserType.MAP -> BlockWrapper(world.name, x, y, z) to BlockWrapper::class.createType()
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