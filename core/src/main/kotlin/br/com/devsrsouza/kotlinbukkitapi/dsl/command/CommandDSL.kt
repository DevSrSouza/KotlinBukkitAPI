package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

typealias ExecutorBlock<T> = suspend Executor<T>.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> List<String>
typealias CommandBuilderBlock = CommandDSL.() -> Unit

fun WithPlugin<*>.simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        description: String = "",
        block: ExecutorBlock<CommandSender>
) = plugin.simpleCommand(name, *aliases, description = description, block = block)

fun Plugin.simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        description: String = "",
        block: ExecutorBlock<CommandSender>
) = simpleCommand(name, *aliases, plugin = this, description = description, block = block)

fun simpleCommand(
        name: String,
        vararg aliases: String = arrayOf(),
        plugin: Plugin,
        description: String = "",
        block: ExecutorBlock<CommandSender>
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
) = CommandDSL(plugin, name, *aliases).apply(block).apply {
    register(plugin)
}

class Executor<E : CommandSender>(
        val sender: E,
        val label: String,
        val args: Array<out String>,
        val command: CommandDSL,
        val scope: CoroutineScope
)

class TabCompleter(
        val sender: CommandSender,
        val alias: String,
        val args: Array<out String>
)

open class CommandDSL(
        private val plugin: Plugin,
        name: String,
        vararg aliases: String = arrayOf(),
        executor: ExecutorBlock<CommandSender>? = null,
        var errorHandler: ErrorHandler = defaultErrorHandler,
        private val job: Job = SupervisorJob(),
        private val coroutineScope: CoroutineScope = CoroutineScope(job + plugin.BukkitDispatchers.SYNC)
) : org.bukkit.command.Command(name.trim()) {

    var onlyInGameMessage = ""
    var cancelOnPlayerDisconnect = true

    init {
        this.aliases = aliases.toList()
    }

    private val jobsFromPlayers by lazy { plugin.onlinePlayerMapOf<Job>() }
    private var executor: ExecutorBlock<CommandSender>? = executor
    private var tabCompleter: TabCompleterBlock? = null

    private val executors: MutableMap<KClass<out CommandSender>, ExecutorBlock<CommandSender>> = mutableMapOf()

    val subCommands: MutableList<CommandDSL> = mutableListOf()

    fun TabCompleter.default() = defaultTabComplete(sender, alias, args)

    fun errorHandler(handler: ErrorHandler) {
        errorHandler = handler
    }

    open fun subCommandBuilder(name: String, vararg aliases: String = arrayOf()): CommandDSL {
        return CommandDSL(
                plugin = plugin,
                name = name,
                aliases = *aliases,
                errorHandler = errorHandler,
                job = job,
                coroutineScope = coroutineScope
        ).also {
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

    open fun executor(block: ExecutorBlock<CommandSender>) {
        executor = block
    }

    open fun executorPlayer(block: ExecutorBlock<Player>) {
        genericExecutor(Player::class, block)
    }

    open fun tabComplete(block: TabCompleterBlock) {
        tabCompleter = block
    }

    open fun <T : CommandSender> genericExecutor(clazz: KClass<T>, block: ExecutorBlock<T>) {
        executors.put(clazz, block as ExecutorBlock<CommandSender>)
    }

    inline fun <reified T : CommandSender> genericExecutor(noinline block: ExecutorBlock<T>) {
        genericExecutor(T::class, block)
    }

    private fun <T> MutableMap<KClass<out CommandSender>, T>.getByInstance(clazz: KClass<*>): T? {
        return entries.find { it.key::class.isInstance(clazz) }?.value
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
            val genericExecutor = executors.getByInstance(sender::class)
            if (genericExecutor != null) {
                coroutineScope.launch {
                    val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                    treatFail(executorModel) {
                        genericExecutor.invoke(executorModel)
                    }
                }
            } else {
                val playerExecutor = executors.getByInstance(Player::class)
                if (playerExecutor != null) {
                    if (sender is Player) {
                        val playerJob = Job() // store and cancel when player
                        if (cancelOnPlayerDisconnect) jobsFromPlayers.put(sender, playerJob, { if (it.isActive) it.cancel() })
                        coroutineScope.launch(playerJob) {
                            val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                            treatFail(executorModel) {
                                playerExecutor.invoke(executorModel as Executor<CommandSender>)
                            }
                        }
                    } else sender.sendMessage(onlyInGameMessage)
                } else {
                    coroutineScope.launch {
                        val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                        treatFail(executorModel) {
                            executor?.invoke(executorModel)
                        }
                    }
                }
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

    private suspend fun treatFail(executor: Executor<*>, block: suspend () -> Unit) {
        try {
            block()
        } catch (ex: CommandFailException) {
            ex.senderMessage?.also { executor.sender.sendMessage(it) }
            ex.execute()
        } catch (ex: Throwable) {
            executor.errorHandler(ex)
        }
    }

}