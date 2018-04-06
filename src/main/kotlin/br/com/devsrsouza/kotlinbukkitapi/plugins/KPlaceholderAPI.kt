package br.com.devsrsouza.kotlinbukkitapi.plugins.placeholderapi

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.regex.Pattern

val hasPlaceholderAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("PlaceholderAPI") != null }

val Player.placeholderAPI get() = KPlaceholderAPI(this)

class KPlaceholderAPI(val player: Player) {
    fun setPlaceholders(message: String) = PlaceholderAPI.setPlaceholders(player, message)
    fun setPlaceholders(message: String, pattern: Pattern) = PlaceholderAPI.setPlaceholders(player, message, pattern)
    fun setPlaceholders(messages: List<String>) = PlaceholderAPI.setPlaceholders(player, messages)
    fun setPlaceholders(messages: List<String>, pattern: Pattern) = PlaceholderAPI.setPlaceholders(player, messages, pattern)
}