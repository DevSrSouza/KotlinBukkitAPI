package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.parser.BlockParser
import br.com.devsrsouza.kotlinbukkitapi.config.parser.ChunkParser
import br.com.devsrsouza.kotlinbukkitapi.config.parser.LocationParser
import br.com.devsrsouza.kotlinbukkitapi.config.parser.ParserType
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

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