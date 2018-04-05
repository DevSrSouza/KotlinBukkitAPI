package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
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

fun command(name: String, vararg aliases: String = arrayOf(),
            description: String = "",
            block: CommandMaker.() -> Unit) {
    val cmdob = object :
        org.bukkit.command.Command(name.trim(), description, "", aliases.toList()) {

        override fun execute(cs: CommandSender, string: String, strings: Array<String>): Boolean {
            try {
                CommandMaker(cs, this, string, strings).block()
            } catch (ex: Exception) {}
            return true
        }
    }
    serverCommands.register(KotlinBukkitAPI.INSTANCE.name, cmdob)
}

class CommandMaker(val sender: CommandSender,
                   val command: Command,
                   val label: String,
                   val args: Array<String>)