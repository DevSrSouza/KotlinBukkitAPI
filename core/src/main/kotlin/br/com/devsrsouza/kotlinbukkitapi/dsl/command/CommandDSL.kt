package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.*
import br.com.devsrsouza.kotlinbukkitapi.utils.whenErrorDefault
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

typealias ExecutorBlock = Executor<CommandSender>.() -> Unit
typealias ExecutorPlayerBlock = Executor<Player>.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> MutableList<String>
typealias CommandMaker = KCommand.() -> Unit

class CommandException(val senderMessage: BaseComponent? = null,
                       val execute: () -> Unit = {},
                       val argMissing: Boolean = false) : RuntimeException() {
    constructor(senderMessage: String = "", execute: () -> Unit = {}, argMissing: Boolean = false)
            : this(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute, argMissing)
}

fun simpleCommand(name: String, vararg aliases: String = arrayOf(),
                  description: String = "",
                  plugin: Plugin = KotlinBukkitAPI.INSTANCE,
                  block: ExecutorBlock) = command(name, *aliases, plugin = plugin) {

    if (description.isNotBlank()) this.description = description

    executor(block)
}

fun command(name: String,
            vararg aliases: String = arrayOf(),
            plugin: Plugin = KotlinBukkitAPI.INSTANCE,
            block: CommandMaker) = KCommand(name, *aliases).apply(block).apply {
    register(plugin)
}

fun <T : CommandSender> Executor<T>.argumentExecutorBuilder(
        posIndex: Int = 1, label: String
) = Executor(
        sender,
        this@argumentExecutorBuilder.label + " " + label,
        whenErrorDefault(emptyArray()) { args.sliceArray(posIndex..args.size) }
)

inline fun <T> Executor<*>.optional(block: () -> T): T? {
    try {
        return block()
    }catch (exception: CommandException) {
        if(exception.argMissing) return null
        else throw exception
    }
}

fun Command.register(plugin: Plugin = KotlinBukkitAPI.INSTANCE) {
    serverCommands.register(plugin.name, this)
}

fun Command.unregister() {
    try {
        val clazz = serverCommands.javaClass
        val f = clazz.getDeclaredField("knownCommands")
        f.isAccessible = true
        val knownCommands = f.get(serverCommands) as MutableMap<String, Command>
        val toRemove = ArrayList<String>()
        for ((key, value) in knownCommands) {
            if (value === this) {
                toRemove.add(key)
            }
        }
        for (str in toRemove) {
            knownCommands.remove(str)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class Executor<E : CommandSender>(val sender: E,
                                  val label: String,
                                  val args: Array<out String>)

class TabCompleter(val sender: CommandSender,
                   val alias: String,
                   val args: Array<out String>)

private val serverCommands: SimpleCommandMap by lazy {
    val packageName = Bukkit.getServer().javaClass.getPackage().getName()
    val version = packageName.substring(packageName.lastIndexOf('.') + 1)
    val bukkitclass = Class.forName("org.bukkit.craftbukkit.$version.CraftServer")
    val f = bukkitclass.getDeclaredField("commandMap")
    f.isAccessible = true
    f.get(Bukkit.getServer()) as SimpleCommandMap
}

open class KCommand(name: String,
                    vararg aliases: String = arrayOf(),
                    executor: ExecutorBlock? = null
) : org.bukkit.command.Command(name.trim()) {

    private var executor: ExecutorBlock? = executor
    private var tabCompleter: TabCompleterBlock? = null

    private val executors: MutableMap<KClass<out CommandSender>, Executor<CommandSender>.() -> Unit> = mutableMapOf()

    val subCommands: MutableList<KCommand> = mutableListOf()

    var onlyInGameMessage = ""

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (!permission.isNullOrBlank() && !sender.hasPermission(permission)) {
            sender.sendMessage(permissionMessage)
        } else {
            if (subCommands.isNotEmpty()) {
                val subCommand = args.getOrNull(0)?.let { arg ->
                    subCommands.find {
                        it.name.equals(arg, true) ||
                                it.aliases.find { it.equals(arg, true) } != null
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
                    genericExecutor.invoke(Executor(sender, label, args))
                } else {
                    val hasPlayer = executors.getByInstance(Player::class)
                    if (hasPlayer != null) {
                        if (executor != null) {
                            executor?.invoke(Executor(sender, label, args))
                        } else sender.sendMessage(onlyInGameMessage)
                    } else {
                        executor?.invoke(Executor(sender, label, args))
                    }
                }
            } catch (ex: CommandException) {
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

    open fun command(name: String, vararg aliases: String = arrayOf(), block: CommandMaker) {
        subCommands.add(KCommand(name).also {
            it.permission = this.permission
            it.permissionMessage = this.permissionMessage
            it.onlyInGameMessage = this.onlyInGameMessage
            it.usageMessage = this.usageMessage
        }.apply(block))
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

}