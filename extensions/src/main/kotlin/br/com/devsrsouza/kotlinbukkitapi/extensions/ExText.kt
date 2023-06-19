package br.com.devsrsouza.kotlinbukkitapi.extensions

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KProperty
import net.md_5.bungee.api.ChatColor as BungeeColor

public fun CommandSender.msg(message: String): Unit = sendMessage(message)
public fun CommandSender.msg(message: Array<String>): Unit = sendMessage(*message)

/**
 * ONLY WITH SPIGOT
 */

public fun textOf(text: String): TextComponent = text.asText()

public fun String.translateColor(code: Char = '&'): String = ChatColor.translateAlternateColorCodes(code, this)
public fun Collection<String>.translateColor(code: Char = '&'): List<String> = map { it.translateColor(code) }
public fun <V> Map<String, V>.translateColorKeys(code: Char = '&'): Map<String, V> = mapKeys { it.key.translateColor(code) }
public fun <K> Map<K, String>.translateColorValues(code: Char = '&'): Map<K, String> = mapValues { it.value.translateColor(code) }

public fun String.reverseTranslateColor(code: Char = '&'): String = replace('§', code)
public fun Collection<String>.reverseTranslateColor(code: Char = '&'): List<String> = map { it.reverseTranslateColor(code) }
public fun <V> Map<String, V>.reverseTranslateColorKeys(code: Char = '&'): Map<String, V> = mapKeys { it.key.reverseTranslateColor(code) }
public fun <K> Map<K, String>.reverseTranslateColorValues(code: Char = '&'): Map<K, String> = mapValues { it.value.reverseTranslateColor(code) }

public operator fun ChatColor.plus(text: String): String = toString() + text
public operator fun ChatColor.plus(other: ChatColor): String = toString() + other.toString()

public fun translateColor(default: String, colorChar: Char = '&'): TranslateChatColorDelegate = TranslateChatColorDelegate(default, colorChar)

public class TranslateChatColorDelegate(public val realValue: String, public val colorChar: Char = '&') {
    private var _value: String = ChatColor.translateAlternateColorCodes(colorChar, realValue)

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): String = _value

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        _value = ChatColor.translateAlternateColorCodes(colorChar, value)
    }
}

public fun Player.sendMessage(text: BaseComponent): Unit = spigot().sendMessage(text)
public fun Player.sendMessage(text: Array<BaseComponent>): Unit = spigot().sendMessage(TextComponent(*text))
public fun Player.sendMessage(text: List<BaseComponent>): Unit = sendMessage(text.toTypedArray())
public fun CommandSender.sendMessage(text: BaseComponent): Unit = (this as? Player)?.let { it.sendMessage(text) } ?: sendMessage(TextComponent.toLegacyText(text))
public fun CommandSender.sendMessage(text: Array<BaseComponent>): Unit = (this as? Player)?.let { it.sendMessage(text) } ?: sendMessage(TextComponent.toLegacyText(*text))
public fun CommandSender.sendMessage(text: List<BaseComponent>): Unit = sendMessage(text.toTypedArray())

public fun Player.msg(text: BaseComponent): Unit = sendMessage(text)
public fun Player.msg(text: Array<BaseComponent>): Unit = sendMessage(text)
public fun Player.msg(text: List<BaseComponent>): Unit = sendMessage(text)
public fun CommandSender.msg(text: BaseComponent): Unit = sendMessage(text)
public fun CommandSender.msg(text: Array<BaseComponent>): Unit = sendMessage(text)
public fun CommandSender.msg(text: List<BaseComponent>): Unit = sendMessage(text)

public fun String.asText(): TextComponent = TextComponent(*TextComponent.fromLegacyText(this))
public fun List<String>.asText(): TextComponent = TextComponent(*map { it.asText() }.toTypedArray())

// fun String.asText() = TextComponent(this)
public fun BaseComponent.toJson(): String = ComponentSerializer.toString(this)
public fun Array<BaseComponent>.toJson(): String = ComponentSerializer.toString(*this)

public operator fun <T : BaseComponent> T.plus(component: BaseComponent): T = apply { addExtra(component) }
public operator fun <T : BaseComponent> T.plus(text: String): T = apply { addExtra(text) }

public fun String.color(color: BungeeColor): TextComponent = asText().color(color)
public fun String.color(color: ChatColor): TextComponent = asText().color(BungeeColor.getByChar(color.char))
public fun String.bold(): TextComponent = asText().bold()
public fun String.italic(): TextComponent = asText().italic()
public fun String.underline(): TextComponent = asText().underline()
public fun String.strikethrough(): TextComponent = asText().strikethrough()
public fun String.obfuscated(): TextComponent = asText().obfuscated()

public fun String.click(clickEvent: ClickEvent): TextComponent = asText().click(clickEvent)
public fun String.openUrl(url: String): TextComponent = click(ClickEvent(ClickEvent.Action.OPEN_URL, url))
public fun String.runCommand(command: String): TextComponent = click(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
public fun String.suggestCommand(command: String): TextComponent = click(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))

public fun String.hover(hoverEvent: HoverEvent): TextComponent = asText().hover(hoverEvent)
public fun String.showText(component: BaseComponent): TextComponent = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
public fun String.showText(vararg components: BaseComponent): TextComponent = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, components))

/**
 * Chaining methods for TextComponent and BaseComponent
 */

public fun TextComponent.append(text: String): TextComponent = apply { addExtra(text) }
public fun TextComponent.append(text: BaseComponent): TextComponent = apply { addExtra(text) }
public fun TextComponent.breakLine(): TextComponent = apply { addExtra("\\n") }

public fun <T : BaseComponent> T.color(color: BungeeColor): T = apply { this.color = color }
public fun <T : BaseComponent> T.color(color: ChatColor): T = apply { this.color = BungeeColor.getByChar(color.char) }
public fun <T : BaseComponent> T.bold(): T = apply { isBold = true }
public fun <T : BaseComponent> T.italic(): T = apply { isItalic = true }
public fun <T : BaseComponent> T.underline(): T = apply { isUnderlined = true }
public fun <T : BaseComponent> T.strikethrough(): T = apply { isStrikethrough = true }
public fun <T : BaseComponent> T.obfuscated(): T = apply { isObfuscated = true }

public fun <T : BaseComponent> T.click(clickEvent: ClickEvent): T = apply { this.clickEvent = clickEvent }
public fun <T : BaseComponent> T.openUrl(url: String): T = click(ClickEvent(ClickEvent.Action.OPEN_URL, url))
public fun <T : BaseComponent> T.runCommand(command: String): T = click(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
public fun <T : BaseComponent> T.suggestCommand(command: String): T = click(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))

public fun <T : BaseComponent> T.hover(hoverEvent: HoverEvent): T = apply { this.hoverEvent = hoverEvent }
public fun <T : BaseComponent> T.showText(component: BaseComponent): T = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(component)))
public fun <T : BaseComponent> T.showText(vararg components: BaseComponent): T = hover(HoverEvent(HoverEvent.Action.SHOW_TEXT, components))

/**
 * Replaces
 */

/**
 * Replace current [TextComponent.text], [BaseComponent.hover] text
 * and [BaseComponent.click] text using [String.replace].
 */
public fun <T : BaseComponent> T.replace(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    replaceOnHover(oldValue, newValue, ignoreCase)
    replaceOnClick(oldValue, newValue, ignoreCase)
    (this as? TextComponent)?.replaceOnText(oldValue, newValue, ignoreCase)
}

/**
 * Replace the current [BaseComponent] text, hover and click and all the [BaseComponent.extra]
 *
 * See [BaseComponent.replace].
 */
public fun <T : BaseComponent> T.replaceAll(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    replaceOnAllHovers(oldValue, newValue, ignoreCase)
    replaceOnAllClick(oldValue, newValue, ignoreCase)
    (this as? TextComponent)?.replaceOnAllTexts(oldValue, newValue, ignoreCase)
}

/**
 * Replace the current [BaseComponent] hover [oldValue] to [newValue] with [String.replace]
 */
public fun <T : BaseComponent> T.replaceOnHover(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    if (hoverEvent != null) {
        for (component in hoverEvent.value)
            if (component is TextComponent) component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
    }
}

/**
 * Replace the current [BaseComponent] hover and all the [BaseComponent.extra]
 *
 * See [BaseComponent.replaceOnHover]
 */
public fun <T : BaseComponent> T.replaceOnAllHovers(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T {
    return apply {
        replaceOnHover(oldValue, newValue, ignoreCase)
        if (extra != null) {
            for (component in extra)
                component.replaceOnAllHovers(oldValue, newValue, ignoreCase)
        }
    }
}

public fun <T : BaseComponent> T.replaceOnShowText(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    if (hoverEvent != null) {
        if (hoverEvent.action == HoverEvent.Action.SHOW_TEXT) {
            for (component in hoverEvent.value)
                if (component is TextComponent) {
                    component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
                }
        }
    }
}
public fun <T : BaseComponent> T.replaceOnAllShowText(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    replaceOnShowText(oldValue, newValue, ignoreCase)
    if (extra != null) {
        for (component in extra) component.replaceOnAllShowText(oldValue, newValue, ignoreCase)
    }
}

/**
 * Replace the current [BaseComponent] click [oldValue] to [newValue] with [String.replace]
 */
public fun <T : BaseComponent> T.replaceOnClick(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    if (clickEvent != null) {
        click(ClickEvent(clickEvent.action, clickEvent.value.replace(oldValue, newValue, ignoreCase)))
    }
}

/**
 * Replace the current [BaseComponent] click and all [BaseComponent.extra]
 *
 * See [BaseComponent.replaceOnClick]
 */
public fun <T : BaseComponent> T.replaceOnAllClick(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    replaceOnClick(oldValue, newValue, ignoreCase)
    if (extra != null) {
        for (component in extra)
            component.replaceOnAllClick(oldValue, newValue, ignoreCase)
    }
}

public fun <T : BaseComponent> T.replaceOnRunCommand(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    if (clickEvent != null && clickEvent.action == ClickEvent.Action.RUN_COMMAND) {
        runCommand(clickEvent.value.replace(oldValue, newValue, ignoreCase))
    }
}
public fun <T : BaseComponent> T.replaceOnAllRunCommand(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): T = apply {
    replaceOnRunCommand(oldValue, newValue, ignoreCase)
    if (extra != null) {
        for (component in extra)
            component.replaceOnRunCommand(oldValue, newValue, ignoreCase)
    }
}

/**
 * Replace the [TextComponent.text] with [String.replace]
 */
public fun TextComponent.replaceOnText(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): TextComponent = apply {
    this.text = text.replace(oldValue, newValue, ignoreCase)
}

/**
 * Replace the current [TextComponent.text] and all the [TextComponent.extra].
 *
 * See [TextComponent.replaceOnText]
 */
public fun TextComponent.replaceOnAllTexts(
    oldValue: String,
    newValue: String,
    ignoreCase: Boolean = false,
): TextComponent = apply {
    replaceOnText(oldValue, newValue, ignoreCase)
    if (extra != null) {
        for (component in extra)
            if (component is TextComponent) {
                component.replaceOnAllTexts(oldValue, newValue, ignoreCase)
            }
    }
}

/**
 * Replace the current [TextComponent.text] [oldValue] to [newValue].
 */
public fun TextComponent.replace(
    oldValue: String,
    newValue: TextComponent,
    ignoreCase: Boolean = false,
): TextComponent = split(oldValue, ignoreCase = ignoreCase, limit = 0).joinToText(newValue)

/**
 * Replace the current [TextComponent.text] and all the [TextComponent.extra] [oldValue] to [newValue].
 */
public fun TextComponent.replaceAll(
    oldValue: String,
    newValue: TextComponent,
    ignoreCase: Boolean = false,
): TextComponent = replace(oldValue, newValue, ignoreCase).apply {
    if (extra != null) {
        extra = extra.map {
            if (it is TextComponent) {
                it.replaceAll(oldValue, newValue, ignoreCase)
            } else {
                it
            }
        }.toMutableList()
    }
}

/**
 * Splits this [TextComponent.text] into a list of [TextComponent] around occurrences of the specified [delimiters].
 *
 * @param delimiters One or more characters to be used as delimiters.
 * @param ignoreCase `true` to ignore character case when matching a delimiter. By default `false`.
 * @param limit The maximum number of substrings to return.
 */
public fun TextComponent.split(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
): List<TextComponent> {
    val copy = duplicate().apply { extra?.clear() }
    val list = text.split(*delimiters, ignoreCase = ignoreCase, limit = limit).map {
        (copy.duplicate() as TextComponent).apply {
            text = it
        }
    }
    if (list.isNotEmpty()) {
        duplicate().also { if (it.extra != null) list.last().extra = it.extra }
    }
    return list
}

/**
 * Creates a [TextComponent] from all the elements separated using [separator] (none by default)
 * and using the given [prefix] (none by default) and [postfix] (none by default).
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
 * elements will be appended.
 */
public fun List<TextComponent>.joinToText(
    separator: TextComponent? = null,
    prefix: TextComponent? = null,
    suffix: TextComponent? = null,
    limit: Int = -1,
): TextComponent {
    if (isEmpty()) return TextComponent("")

    var component = prefix?.append(get(0)) ?: get(0)

    var count = 0
    for (element in this) {
        if (++count > 1 && separator != null) component.append(separator)
        if (limit < 0 || count <= limit) {
            if (count > 1) component.append(element)
        } else {
            break
        }
    }
    if (suffix != null) component.append(suffix)
    return component
}
