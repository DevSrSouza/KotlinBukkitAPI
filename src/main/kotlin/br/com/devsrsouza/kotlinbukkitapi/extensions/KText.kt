package br.com.devsrsouza.kotlinbukkitapi.extensions.text

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.md_5.bungee.api.ChatColor as BungeeColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * ONLY WITH SPIGOT
 */

operator fun String.unaryPlus() = ChatColor.translateAlternateColorCodes('&', this)
operator fun String.unaryMinus() = replace('§', '&')

fun Player.sendMessage(text: BaseComponent) = spigot().sendMessage(text)
fun Player.sendMessage(text: Array<BaseComponent>) = spigot().sendMessage(TextComponent(*text))
fun Player.sendMessage(text: List<BaseComponent>) = spigot().sendMessage(TextComponent(*text.toTypedArray()))

//fun String.asText() = TextComponent(*TextComponent.fromLegacyText(this))
fun String.asText() = TextComponent(this)
fun BaseComponent.toJson() = ComponentSerializer.toString(this)
fun Array<BaseComponent>.toJson() = ComponentSerializer.toString(*this)

operator fun String.plus(text: BaseComponent) = asText().apply { addExtra(text) }
operator fun BaseComponent.plus(component: BaseComponent) = apply { addExtra(component) }
operator fun BaseComponent.plus(text: String) = apply { addExtra(text) }

fun String.color(color: BungeeColor) = asText().color(color)
fun String.bold() = asText().bold()
fun String.italic() = asText().italic()
fun String.underline() = asText().underline()
fun String.strikethrough() = asText().strikethrough()
fun String.obfuscated() = asText().obfuscated()

fun String.click(clickEvent: ClickEvent) = asText().click(clickEvent)
fun String.openUrl(url: String) = click(ClickEvent(ClickEvent.Action.OPEN_URL, url))
fun String.runCommand(command: String) = click(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
fun String.suggestCommand(command: String) = click(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))

fun String.hover(hoverEvent: HoverEvent) = asText().hover(hoverEvent)
fun String.showText(component: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
fun String.showText(vararg components: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, components))

/**
 * Chaining methods for TextComponent and BaseComponent
 */

fun TextComponent.append(text: String) = apply { addExtra(text) }
fun TextComponent.append(text: BaseComponent) = apply { addExtra(text) }
fun TextComponent.breakLine() = apply { addExtra("\n") }

fun <T : BaseComponent> T.color(color: BungeeColor) = apply { this.color = color }
fun <T : BaseComponent> T.bold() = apply { isBold = true }
fun <T : BaseComponent> T.italic() = apply { isItalic = true }
fun <T : BaseComponent> T.underline() = apply { isUnderlined = true }
fun <T : BaseComponent> T.strikethrough() = apply { isStrikethrough = true }
fun <T : BaseComponent> T.obfuscated() = apply { isObfuscated = true }

fun <T : BaseComponent> T.click(clickEvent: ClickEvent) = apply { this.clickEvent = clickEvent }
fun <T : BaseComponent> T.openUrl(url: String) = click(ClickEvent(ClickEvent.Action.OPEN_URL, url))
fun <T : BaseComponent> T.runCommand(command: String) = click(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
fun <T : BaseComponent> T.suggestCommand(command: String) = click(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))

fun <T : BaseComponent> T.hover(hoverEvent: HoverEvent) = apply { this.hoverEvent = hoverEvent }
fun <T : BaseComponent> T.showText(component: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
fun <T : BaseComponent> T.showText(vararg components: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, components))
