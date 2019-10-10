package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

typealias ExecutorBlock = Executor<CommandSender>.() -> Unit
typealias ExecutorPlayerBlock = Executor<Player>.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> List<String>
typealias CommandBuilderBlock = CommandDSL.() -> Unit

fun WithPlugin<*>.simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        description: String = "",
        block: ExecutorBlock
) = plugin.simpleCommand(name, *aliases, description = description, block = block)

fun Plugin.simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        description: String = "",
        block: ExecutorBlock
) = simpleCommand(name, *aliases, plugin = this, description = description, block = block)

fun simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        plugin: Plugin,
        description: String = "",
        block: ExecutorBlock
) = command(name, *aliases, plugin = plugin) {
    if (description.isNotBlank()) this.description = description

    executor(block)
}

inline fun WithPlugin<*>.command(
        name: String,
        vararg aliases: String = arrayOf(),
        block: CommandBuilderBlock
) = plugin.command(name, *aliases, block = block)

inline fun Plugin.command(
        name: String,
        vararg aliases: String = arrayOf(),
        block: CommandBuilderBlock
) = command(name, *aliases, plugin = this, block = block)

inline fun command(
        name: String,
        vararg aliases: String = arrayOf(),
        plugin: Plugin,
        block: CommandBuilderBlock
) = CommandDSL(name, *aliases).apply(block).apply {
    register(plugin)
}

class Executor<E : CommandSender>(
        val sender: E,
        val label: String,
        val args: Array<out String>,
        val command: CommandDSL
)

class TabCompleter(
        val sender: CommandSender,
        val alias: String,
        val args: Array<out String>
)

open class CommandDSL(
        name: String,
        vararg aliases: String = arrayOf(),
        executor: ExecutorBlock? = null
) : org.bukkit.command.Command(name.trim()) {

    var onlyInGameMessage = ""

    init {
        this.aliases = aliases.toList()
    }

    private var executor: ExecutorBlock? = executor
    private var tabCompleter: TabCompleterBlock? = null

    private val executors: MutableMap<KClass<out CommandSender>, Executor<CommandSender>.() -> Unit> = mutableMapOf()

    val subCommands: MutableList<CommandDSL> = mutableListOf()

    fun TabCompleter.default() = defaultTabComplete(sender, alias, args)

    open fun subCommandBuilder(name: String, vararg aliases: String = arrayOf()): CommandDSL {
        return CommandDSL(name, *aliases).also {
            it.permission = this.permission
            it.permissionMessage = this.permissionMessage
            it.onlyInGameMessage = this.onlyInGameMessage
            it.usageMessage = this.usageMessage
        }
    }

    inline fun command(
            name: String,
            vararg aliases: String = arrayOf(),
            block: CommandBuilderBlock
    ): CommandDSL {
        return subCommandBuilder(name, *aliases).apply(block).also { subCommands.add(it) }
    }

    open fun executor(block: ExecutorBlock) {
        executor = block
    }

    open fun executorPlayer(block: ExecutorPlayerBlock) {
        genericExecutor(Player::class, block)
    }

    open fun tabComplete(block: TabCompleterBlock) {
        tabCompleter = block
    }

    open fun <T : CommandSender> genericExecutor(clazz: KClass<T>, block: Executor<T>.() -> Unit) {
        executors.put(clazz, block as Executor<CommandSender>.() -> Unit)
    }

    inline fun <reified T : CommandSender> genericExecutor(noinline block: Executor<T>.() -> Unit) {
        genericExecutor(T::class, block)
    }

    private fun <T> MutableMap<KClass<out CommandSender>, T>.getByInstance(clazz: KClass<*>): T? {
        return entries.find { clazz.isSubclassOf(it.key) }?.value
    }

    override fun execute(
            sender: CommandSender,
            label: String,
            args: Array<out String>
    ): Boolean {
        if (!permission.isNullOrBlank() && !sender.hasPermission(permission)) {
            sender.sendMessage(permissionMessage)
        } else {
            if (subCommands.isNotEmpty()) {
                val subCommand = args.getOrNull(0)?.let { arg ->
                    subCommands.find {
                        it.name.equals(arg, true) ||
                                it.aliases.any { it.equals(arg, true) }
                    }
                }
                if (subCommand != null) {
                    subCommand.execute(sender, "$label ${args.get(0)}", args.sliceArray(1 until args.size))
                    return true
                }
            }
            try {
                val genericExecutor = executors.getByInstance(sender::class)
                if (genericExecutor != null) {
                    genericExecutor.invoke(Executor(sender, label, args, this))
                } else {
                    val hasPlayer = executors.getByInstance(Player::class)
                    if (hasPlayer != null) {
                        if (executor != null) {
                            executor?.invoke(Executor(sender, label, args, this))
                        } else sender.sendMessage(onlyInGameMessage)
                    } else {
                        executor?.invoke(Executor(sender, label, args, this))
                    }
                }
            } catch (ex: CommandFailException) {
                ex.senderMessage?.also { sender.sendMessage(it) }
                ex.execute()
            }
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        return if (tabCompleter != null) {
            tabCompleter!!.invoke(TabCompleter(sender, alias, args))
        } else {
            defaultTabComplete(sender, alias, args)
        }
    }

    open fun defaultTabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        if (args.size > 1) {
            val subCommand = subCommands.find { it.name.equals(args.getOrNull(0), true) }
            if (subCommand != null) {
                return subCommand.tabComplete(sender, args.get(0), args.sliceArray(1 until args.size))
            } else {
                emptyList<String>()
            }
        } else if (args.size > 0) {
            if (subCommands.isNotEmpty()) {
                return subCommands
                        .filter { it.name.startsWith(args.get(0), true) }
                        .map { it.name }
            } else return super.tabComplete(sender, alias, args)
        }
        return super.tabComplete(sender, alias, args)
    }

}