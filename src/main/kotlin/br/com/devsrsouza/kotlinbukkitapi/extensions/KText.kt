package br.com.devsrsouza.kotlinbukkitapi.extensions.text

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.md_5.bungee.api.ChatColor as BungeeColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player

operator fun String.unaryPlus() = ChatColor.translateAlternateColorCodes('&', this)
operator fun String.unaryMinus() = replace('§', '&')

fun Player.sendRawMessage(text: BaseComponent) = sendRawMessage(text.toJson())
fun Player.sendRawMessage(text: Array<BaseComponent>) = sendRawMessage(text.toJson())

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
 * BUILDER STYLE
 */

fun TextComponent.append(text: String) = apply { addExtra(text) }
fun TextComponent.append(text: BaseComponent) = apply { addExtra(text) }
fun TextComponent.breakLine() = apply { addExtra("\n") }

fun BaseComponent.color(color: BungeeColor) = apply { this.color = color }
fun BaseComponent.bold() = apply { isBold = true }
fun BaseComponent.italic() = apply { isItalic = true }
fun BaseComponent.underline() = apply { isUnderlined = true }
fun BaseComponent.strikethrough() = apply { isStrikethrough = true }
fun BaseComponent.obfuscated() = apply { isObfuscated = true }

fun BaseComponent.click(clickEvent: ClickEvent) = apply { this.clickEvent = clickEvent }
fun BaseComponent.openUrl(url: String) = click(ClickEvent(ClickEvent.Action.OPEN_URL, url))
fun BaseComponent.runCommand(command: String) = click(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
fun BaseComponent.suggestCommand(command: String) = click(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))

fun BaseComponent.hover(hoverEvent: HoverEvent) = apply { this.hoverEvent = hoverEvent }
fun BaseComponent.showText(component: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
fun BaseComponent.showText(vararg components: BaseComponent) = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, components))