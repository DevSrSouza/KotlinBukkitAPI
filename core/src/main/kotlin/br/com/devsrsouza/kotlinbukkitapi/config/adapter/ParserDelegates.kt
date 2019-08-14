package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.parser.*
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.material.MaterialData

fun location(
        default: Location,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, LocationParser(type))

fun block(
        default: Block,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, BlockParser(type))

fun chunk(
        default: Chunk,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, ChunkParser(type))

fun materialData(
        default: MaterialData
) = ObjectParserDelegate(default, MaterialDataParser)