package br.com.devsrsouza.kotlinbukkitapi.extensions.text

import net.kyori.text.TextComponent
import net.kyori.text.event.ClickEvent
import net.kyori.text.event.HoverEvent
import net.kyori.text.format.TextColor
import net.kyori.text.format.TextDecoration
import net.kyori.text.serializer.GsonComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

private val gsonSerializer = GsonComponentSerializer()

fun Player.sendMessage(text: TextComponent) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw $name ${gsonSerializer.serialize(text)}")
}

fun String.color(color: TextColor) = TextComponent.builder(this).color(color).build()
fun String.style(styles: TextDecoration) = TextComponent.builder(this).decoration(styles, true).build()
fun String.click(clickAction: ClickEvent) = TextComponent.builder(this).clickEvent(clickAction).build()
fun String.hover(hoverAction: HoverEvent) = TextComponent.builder(this).hoverEvent(hoverAction).build()

fun String.asText() = TextComponent.builder(this).build()

operator fun String.plus(text: TextComponent)
        = TextComponent.builder().append(TextComponent.of(this)).append(text).build()
operator fun TextComponent.plus(text: TextComponent) = append(text)
operator fun TextComponent.plus(text: String) = append(TextComponent.of(text))

operator fun String.unaryPlus() = ChatColor.translateAlternateColorCodes('&', this)
operator fun String.unaryMinus() = replace('§', '&')