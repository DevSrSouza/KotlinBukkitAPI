package br.com.devsrsouza.kotlinbukkitapi.plugins.placeholderapi

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.click
import me.clip.placeholderapi.PlaceholderAPI
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.regex.Pattern

val hasPlaceholderAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("PlaceholderAPI") != null }

val Player.placeholderAPI get() = KPlaceholderAPI(this)

inline class KPlaceholderAPI(val player: Player) {
    fun setPlaceholders(message: String) = PlaceholderAPI.setPlaceholders(player, message)
    fun setPlaceholders(message: String, pattern: Pattern) = PlaceholderAPI.setPlaceholders(player, message, pattern)
    fun setPlaceholders(messages: List<String>) = PlaceholderAPI.setPlaceholders(player, messages)
    fun setPlaceholders(messages: List<String>, pattern: Pattern) = PlaceholderAPI.setPlaceholders(player, messages, pattern)

    fun sendMessage(message: String) = player.sendMessage(setPlaceholders(message))
    fun sendMessage(message: List<String>) = setPlaceholders(message).forEach { player.sendMessage(it) }

    fun <T : BaseComponent> setPlaceholders(component: T): T {
        if (component is TextComponent)
            component.text = setPlaceholders(component.text)

        if (component.clickEvent != null)
            component.click(ClickEvent(component.clickEvent.action, setPlaceholders(component.clickEvent.value)))

        if (component.hoverEvent != null)
            for (innerComponent in component.hoverEvent.value)
                if (innerComponent is TextComponent) setPlaceholders(innerComponent)

        if (component.extra != null)
            for (innerComponent in component.extra)
                setPlaceholders(innerComponent)

        return component
    }

    fun sendMessage(text: BaseComponent) = player.spigot().sendMessage(setPlaceholders(text))
    fun sendMessage(text: Array<BaseComponent>) = sendMessage(TextComponent(*text))
}