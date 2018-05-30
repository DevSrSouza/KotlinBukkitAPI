package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap

private val serverCommands : SimpleCommandMap by lazy {
    val packageName = Bukkit.getServer().javaClass.getPackage().getName()
    val version = packageName.substring(packageName.lastIndexOf('.') + 1)
    val bukkitclass = Class.forName("org.bukkit.craftbukkit.$version.CraftServer")
    val f = bukkitclass.getDeclaredField("commandMap")
    f.isAccessible = true
    f.get(Bukkit.getServer()) as SimpleCommandMap
}

typealias ExecuterBlock = Executer.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> MutableList<String>
typealias CommandMaker = KCommand.() -> Unit

fun simpleCommand(name: String, vararg aliases: String = arrayOf(),
            description: String = "",
            block: ExecuterBlock) {
    val cmd = command(name) {

        if(description.isBlank()) this.description = description
        if(aliases.isNotEmpty()) this.aliases = aliases.toList()

        executer(block)
    }
}


fun command(name: String,
            block: CommandMaker) {

    val cmd = KCommand(name).apply(block)

    serverCommands.register(KotlinBukkitAPI.INSTANCE.name, cmd)
}

class Executer(val sender: CommandSender,
               val label: String,
               val args: Array<out String>)

class TabCompleter(val sender: CommandSender,
                   val alias: String,
                   val args: Array<out String>)

class KCommand(name: String,
               executer: ExecuterBlock = {}
) : org.bukkit.command.Command(name.trim()) {

    private var executer: ExecuterBlock = executer
    private var tabCompleter: TabCompleterBlock? = null
    private val subCommands: MutableList<KCommand> = mutableListOf()

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
       if(subCommands.isNotEmpty()) {
            val subCommand = subCommands.find { it.name.equals(args.getOrNull(0), true) }
            if(subCommand != null) {
                subCommand.execute(sender, commandLabel, args.sliceArray(1 until args.size))
            } else if(executer != null) {
                Executer(sender, commandLabel, args).executer()
            }
        } else {
            if(executer != null) Executer(sender, commandLabel, args).executer()
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (tabCompleter != null) {
            return tabCompleter!!.invoke(TabCompleter(sender, alias, args))
        } else {
            if (subCommands.isEmpty()) {
                return super.tabComplete(sender, alias, args)
            } else {
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
            }
            return emptyList<String>().toMutableList()
        }
    }

    fun command(name: String, block: CommandMaker) {
        subCommands.add(KCommand(name).also {
            it.permission = this.permission
            it.permissionMessage = this.permissionMessage
        }.apply(block))
    }

    fun executer(block: ExecuterBlock) {
        executer = block
    }

    fun tabComplete(block: TabCompleterBlock) {
        tabCompleter = block
    }

}