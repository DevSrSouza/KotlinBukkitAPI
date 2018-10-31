package br.com.devsrsouza.kotlinbukkitapi.plugins.dvdwplaceholderapi

import be.maximvdw.placeholderapi.PlaceholderAPI as MVdWPlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import kotlin.reflect.full.safeCast

val hasMVdWPlaceholderAPI by lazy { MVdWPlaceholder != null }

val MVdWPlaceholder by lazy {
    MVdWPlaceholderAPI::class.safeCast(Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI"))
}

val OfflinePlayer.mvdWPlaceholderAPI get() = MVdWPlaceholderAPI(this)

inline class MVdWPlaceholderAPI(val player: OfflinePlayer) {
    fun replacePlaceholders(input: String) = MVdWPlaceholderAPI.replacePlaceholders(player, input)
}