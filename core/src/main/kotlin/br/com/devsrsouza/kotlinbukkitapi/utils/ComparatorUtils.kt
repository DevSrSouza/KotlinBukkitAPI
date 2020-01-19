package br.com.devsrsouza.kotlinbukkitapi.utils

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object PluginComparator : Comparator<Plugin> {
    override fun compare(p0: Plugin, p1: Plugin): Int {
        return p0.name.compareTo(p1.name)
    }
}

object PlayerComparator : Comparator<Player> {
    override fun compare(p0: Player, p1: Player): Int {
        return p0.uniqueId.compareTo(p1.uniqueId)
    }
}