package br.com.devsrsouza.kotlinbukkitapi.extensions.text

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.md_5.bungee.api.ChatColor as BungeeColor
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KProperty

fun CommandSender.msg(message: String) = sendMessage(message)
fun CommandSender.msg(message: Array<String>) = sendMessage(message)

/**
 * ONLY WITH SPIGOT
 */

fun textOf(text: String) = text.asText()

operator fun String.unaryPlus() = ChatColor.translateAlternateColorCodes('&', this)
operator fun String.unaryMinus() = replace('§', '&')
fun translateColor(default: String, colorChar: Char = '&') = TranslateChatColorDelegate(default, colorChar)

class TranslateChatColorDelegate(val realValue: String, val colorChar: Char = '&') {
    private var _value: String = ChatColor.translateAlternateColorCodes(colorChar, realValue)

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = _value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        _value = ChatColor.translateAlternateColorCodes(colorChar, value)
    }
}

fun Player.sendMessage(text: BaseComponent) = spigot().sendMessage(text)
fun Player.sendMessage(text: Array<BaseComponent>) = spigot().sendMessage(TextComponent(*text))
fun Player.sendMessage(text: List<BaseComponent>) = sendMessage(text.toTypedArray())
fun CommandSender.sendMessage(text: BaseComponent) = (this as? Player)?.let { it.sendMessage(text) } ?: sendMessage(TextComponent.toLegacyText(text))
fun CommandSender.sendMessage(text: Array<BaseComponent>) = (this as? Player)?.let { it.sendMessage(text) } ?: sendMessage(TextComponent.toLegacyText(*text))
fun CommandSender.sendMessage(text: List<BaseComponent>) = sendMessage(text.toTypedArray())

fun Player.msg(text: BaseComponent) = sendMessage(text)
fun Player.msg(text: Array<BaseComponent>) = sendMessage(text)
fun Player.msg(text: List<BaseComponent>) = sendMessage(text)
fun CommandSender.msg(text: BaseComponent) = sendMessage(text)
fun CommandSender.msg(text: Array<BaseComponent>) = sendMessage(text)
fun CommandSender.msg(text: List<BaseComponent>) = sendMessage(text)

fun String.asText() = TextComponent(*TextComponent.fromLegacyText(this))
//fun String.asText() = TextComponent(this)
fun BaseComponent.toJson() = ComponentSerializer.toString(this)
fun Array<BaseComponent>.toJson() = ComponentSerializer.toString(*this)

operator fun String.plus(text: BaseComponent) = asText().apply { addExtra(text) }
operator fun <T : BaseComponent> T.plus(component: BaseComponent) = apply { addExtra(component) }
operator fun <T : BaseComponent> T.plus(text: String) = apply { addExtra(text) }

fun String.color(color: BungeeColor) = asText().color(color)
fun String.color(color: ChatColor) = asText().color(BungeeColor.getByChar(color.char))
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
fun <T : BaseComponent> T.color(color: ChatColor) = apply { this.color = BungeeColor.getByChar(color.char) }
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

/**
 * Replaces
 */

fun <T : BaseComponent> T.replace(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    replaceOnHover(oldValue, newValue, ignoreCase)
    replaceOnClick(oldValue, newValue, ignoreCase)
    (this as? TextComponent)?.replaceOnText(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceAll(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    replaceOnAllHovers(oldValue, newValue, ignoreCase)
    replaceOnAllClick(oldValue, newValue, ignoreCase)
    (this as? TextComponent)?.replaceOnAllTexts(oldValue, newValue, ignoreCase)
}

fun <T : BaseComponent> T.replaceOnHover(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    if(hoverEvent != null)
        for (component in hoverEvent.value)
            if (component is TextComponent) component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceOnAllHovers(oldValue: String, newValue: String, ignoreCase: Boolean = false) : T = apply{
    replaceOnHover(oldValue, newValue, ignoreCase)
    if(extra != null)
        for (component in extra)
            component.replaceOnAllHovers(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceOnShowText(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    if(hoverEvent != null)
        if(hoverEvent.action == HoverEvent.Action.SHOW_TEXT)
            for (component in hoverEvent.value)
                if(component is TextComponent)
                    component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceOnAllShowText(oldValue: String, newValue: String, ignoreCase: Boolean = false) : T = apply {
    replaceOnShowText(oldValue, newValue, ignoreCase)
    if(extra != null)
        for (component in extra) component.replaceOnAllShowText(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceOnClick(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    if(clickEvent != null)
        click(ClickEvent(clickEvent.action, clickEvent.value.replace(oldValue, newValue, ignoreCase)))
}
fun <T : BaseComponent> T.replaceOnAllClick(oldValue: String, newValue: String, ignoreCase: Boolean = false) : T = apply {
    replaceOnClick(oldValue, newValue, ignoreCase)
    if(extra != null)
        for (component in extra)
            component.replaceOnAllClick(oldValue, newValue, ignoreCase)
}
fun <T : BaseComponent> T.replaceOnRunCommand(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    if(clickEvent != null && clickEvent.action == ClickEvent.Action.RUN_COMMAND)
        runCommand(clickEvent.value.replace(oldValue, newValue, ignoreCase))
}
fun <T : BaseComponent> T.replaceOnAllRunCommand(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    replaceOnRunCommand(oldValue, newValue, ignoreCase)
    if(extra != null)
        for (component in extra)
            component.replaceOnRunCommand(oldValue, newValue, ignoreCase)
}

fun TextComponent.replaceOnText(oldValue: String, newValue: String, ignoreCase: Boolean = false) = apply {
    this.text = text.replace(oldValue, newValue, ignoreCase)
}
fun TextComponent.replaceOnAllTexts(oldValue: String, newValue: String, ignoreCase: Boolean = false) : TextComponent = apply {
    replaceOnText(oldValue, newValue, ignoreCase)
    if(extra != null)
        for (component in extra)
            if(component is TextComponent)
                component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
}

fun TextComponent.replace(oldValue: String, newValue: TextComponent, ignoreCase: Boolean = false)
        = split(oldValue, ignoreCase = ignoreCase, limit = 0).joinToText(newValue)

fun TextComponent.replaceAll(oldValue: String, newValue: TextComponent, ignoreCase: Boolean = false) : TextComponent
        = replace(oldValue, newValue, ignoreCase).apply {
    if(extra != null)
        extra = extra.map {
            if(it is TextComponent)
                it.replaceAll(oldValue, newValue, ignoreCase)
            else it
        }.toMutableList()
}

fun TextComponent.split(vararg delimiters: String, ignoreCase: Boolean = false, limit: Int = 0): List<TextComponent> {
    val copy = duplicate().apply { extra?.clear() }
    val list = text.split(*delimiters, ignoreCase = ignoreCase, limit = limit).map {
        (copy.duplicate() as TextComponent).apply {
            text = it
        }
    }
    if(list.isNotEmpty()) {
        duplicate().also { if(it.extra != null) list.last().extra = it.extra }
    }
    return list
}

fun List<TextComponent>.joinToText(separator: TextComponent? = null,
                                   prefix: TextComponent? = null,
                                   suffix: TextComponent? = null,
                                   limit: Int = -1) : TextComponent {
    if(isEmpty()) return TextComponent("")

    var component = prefix?.append(get(0)) ?: get(0)

    var count = 0
    for (element in this) {
        if (++count > 1 && separator != null) component.append(separator)
        if (limit < 0 || count <= limit) {
            if(count > 1) component.append(element)
        } else break
    }
    if(suffix != null) component.append(suffix)
    return component
}