package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.msg
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.suggestCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

const val SEND_SUB_COMMANDS_LABEL_PLACEHOLDER = "{label}"
const val SEND_SUB_COMMANDS_NAME_PLACEHOLDER = "{subcmd}"
const val SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER = "{description}"

val SEND_SUB_COMMANDS_DEFAULT_FORMAT = "${ChatColor.BLUE}/$SEND_SUB_COMMANDS_LABEL_PLACEHOLDER ${ChatColor.YELLOW}$SEND_SUB_COMMANDS_NAME_PLACEHOLDER ${ChatColor.BLUE}-> ${ChatColor.GRAY}$SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER"

val Executor<Player>.player: Player get() = sender

fun Executor<*>.sendSubCommandsList(
    format: String = SEND_SUB_COMMANDS_DEFAULT_FORMAT,
    needCommandPermission: Boolean = true
) {
    val subcmds = command.subCommands.filterNot {
        needCommandPermission && !sender.hasPermission(it.permission)
    }.associateWith {
        format.replace(SEND_SUB_COMMANDS_LABEL_PLACEHOLDER, label, true)
            .replace(SEND_SUB_COMMANDS_NAME_PLACEHOLDER, it.name, true)
            .replace(SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER, it.description, true)
    }
    if(subcmds.isEmpty()) fail(command.permissionMessage)

    if(sender is Player) {
        subcmds.map { (key, value) ->
            value.suggestCommand("/$label ${key.name}")
        }.forEach { sender.msg(it) }
    } else {
        subcmds.values.forEach { sender.msg(it) }
    }
}