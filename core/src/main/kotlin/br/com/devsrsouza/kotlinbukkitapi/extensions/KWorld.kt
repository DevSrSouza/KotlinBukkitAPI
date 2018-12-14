package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity

inline fun <reified T : Entity> World.spawn(location: Location) : T {
    return spawn(location, T::class.java)
}