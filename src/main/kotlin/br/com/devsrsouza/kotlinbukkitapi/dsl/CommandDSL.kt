package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

private val serverCommands : SimpleCommandMap by lazy {
    val packageName = Bukkit.getServer().javaClass.getPackage().getName()
    val version = packageName.substring(packageName.lastIndexOf('.') + 1)
    val bukkitclass = Class.forName("org.bukkit.craftbukkit.$version.CraftServer")
    val f = bukkitclass.getDeclaredField("commandMap")
    f.isAccessible = true
    f.get(Bukkit.getServer()) as SimpleCommandMap
}

typealias ExecutorBlock = Executor<CommandSender>.() -> Unit
typealias ExecutorPlayerBlock = Executor<Player>.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> MutableList<String>
typealias CommandMaker = KCommand.() -> Unit

fun simpleCommand(name: String, vararg aliases: String = arrayOf(),
                  description: String = "",
                  plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                  block: ExecutorBlock) {
    val cmd = command(name, plugin) {

        if (description.isBlank()) this.description = description
        if (aliases.isNotEmpty()) this.aliases = aliases.toList()

        executor(block)
    }
}


fun command(name: String,
            plugin: Plugin = KotlinBukkitAPI.INSTANCE,
            block: CommandMaker) {

    val cmd = KCommand(name).apply(block)

    serverCommands.register(plugin.name, cmd)
}

class Executor<E : CommandSender>(val sender: E,
               val label: String,
               val args: Array<out String>)

class TabCompleter(val sender: CommandSender,
                   val alias: String,
                   val args: Array<out String>)

class KCommand(name: String,
               executor: ExecutorBlock = {}
) : org.bukkit.command.Command(name.trim()) {

    private var executor: ExecutorBlock = executor
    private var executorPlayer: ExecutorPlayerBlock? = null
    private var tabCompleter: TabCompleterBlock? = null
    private val subCommands: MutableList<KCommand> = mutableListOf()

    var onlyInGameMessage = ""

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (subCommands.isNotEmpty()) {
            val subCommand = subCommands.find { it.name.equals(args.getOrNull(0), true) }
            if (subCommand != null) {
                subCommand.execute(sender, commandLabel, args.sliceArray(1 until args.size))
            } else if (executorPlayer != null) {
                if(sender is Player) {
                    executorPlayer?.invoke(Executor(sender, commandLabel, args))
                } else sender.sendMessage(onlyInGameMessage)
            } else {
                Executor(sender, commandLabel, args).executor()
            }
        } else {
            Executor(sender, commandLabel, args).executor()
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return if (tabCompleter != null) {
            tabCompleter!!.invoke(TabCompleter(sender, alias, args))
        } else {
            defaultTabComplete(sender, alias, args)
        }
    }

    fun defaultTabComplete(sender: CommandSender, alias: String, args: Array<out String>) : MutableList<String> {
        if (args.size > 1) {
            val subCommand = subCommands.find { it.name.equals(args.getOrNull(0), true) }
            if (subCommand != null) {
                return subCommand.tabComplete(sender, args.get(0), args.sliceArray(1 until args.size))
            } else {
                emptyList<String>().toMutableList()
            }
        } else if (args.size > 0) {
            return subCommands
                    .filter { it.name.startsWith(args.get(0), true) }
                    .map { it.name }
                    .toMutableList()
        }
        return super.tabComplete(sender, alias, args)
    }

    fun command(name: String, block: CommandMaker) {
        subCommands.add(KCommand(name).also {
            it.permission = this.permission
            it.permissionMessage = this.permissionMessage
        }.apply(block))
    }

    fun executor(block: ExecutorBlock) {
        executor = block
    }

    fun executorPlayer(block: ExecutorPlayerBlock) {
        executorPlayer = block
    }

    fun tabComplete(block: TabCompleterBlock) {
        tabCompleter = block
    }

}