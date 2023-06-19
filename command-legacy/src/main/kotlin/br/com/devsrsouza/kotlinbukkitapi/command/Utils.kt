package br.com.devsrsouza.kotlinbukkitapi.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.msg
import br.com.devsrsouza.kotlinbukkitapi.extensions.suggestCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

public const val SEND_SUB_COMMANDS_LABEL_PLACEHOLDER: String = "{label}"
public const val SEND_SUB_COMMANDS_NAME_PLACEHOLDER: String = "{subcmd}"
public const val SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER: String = "{description}"

public val SEND_SUB_COMMANDS_DEFAULT_FORMAT: String = "${ChatColor.BLUE}/$SEND_SUB_COMMANDS_LABEL_PLACEHOLDER ${ChatColor.YELLOW}$SEND_SUB_COMMANDS_NAME_PLACEHOLDER ${ChatColor.BLUE}-> ${ChatColor.GRAY}$SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER"

public val Executor<Player>.player: Player get() = sender

public fun Executor<*>.sendSubCommandsList(
    format: String = SEND_SUB_COMMANDS_DEFAULT_FORMAT,
    needCommandPermission: Boolean = true,
) {
    val subcmds = command.subCommands.filterNot {
        needCommandPermission && (it.permission != null && sender.hasPermission(it.permission!!).not())
    }.associateWith {
        format.replace(SEND_SUB_COMMANDS_LABEL_PLACEHOLDER, label, true)
            .replace(SEND_SUB_COMMANDS_NAME_PLACEHOLDER, it.name, true)
            .replace(SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER, it.description, true)
    }
    if (subcmds.isEmpty()) fail(command.permissionMessage ?: "")

    if (sender is Player) {
        subcmds.map { (key, value) ->
            value.suggestCommand("/$label ${key.name}")
        }.forEach { sender.msg(it) }
    } else {
        subcmds.values.forEach { sender.msg(it) }
    }
}
