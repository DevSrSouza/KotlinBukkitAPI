package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.parser.LocationParser
import br.com.devsrsouza.kotlinbukkitapi.config.parser.LocationParserType
import org.bukkit.Location

fun location(
        default: Location,
        type: LocationParserType = LocationParserType.MAP
) = ObjectParserDelegate(default, LocationParser(type))