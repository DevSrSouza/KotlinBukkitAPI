package br.com.devsrsouza.kotlinbukkitapi.plugins.dvdwplaceholderapi

import be.maximvdw.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import kotlin.reflect.full.safeCast

val hasMVdWPlaceholderAPI by lazy { MVdWPlaceholder != null }

val MVdWPlaceholder by lazy {
    PlaceholderAPI::class.safeCast(Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI"))
}

val OfflinePlayer.mvdWPlaceholderAPI get() = MVdWPlaceholderAPI(this)

class MVdWPlaceholderAPI(val player: OfflinePlayer) {
    fun replacePlaceholders(input: String) = PlaceholderAPI.replacePlaceholders(player, input)
}