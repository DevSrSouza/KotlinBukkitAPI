package br.com.devsrsouza.kotlinbukkitapi.extensions.command

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

private val serverCommands: SimpleCommandMap by lazy {
    val packageName = Bukkit.getServer().javaClass.getPackage().getName()
    val version = packageName.substring(packageName.lastIndexOf('.') + 1)
    // TODO support for GlowStone
    val bukkitclass = Class.forName("org.bukkit.craftbukkit.$version.CraftServer")
    val f = bukkitclass.getDeclaredField("commandMap")
    f.isAccessible = true
    f.get(Bukkit.getServer()) as SimpleCommandMap
}

private val knownCommandsField: Field by lazy {
    // TODO support for GlowStone
    serverCommands.javaClass.getDeclaredField("knownCommands").apply {
        isAccessible = true
    }
}

internal object CommandsRegisterController : Listener {
    val commands = hashMapOf<String, MutableList<Command>>()

    init {
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach {
                it.unregister()
            }
        }
    }
}

fun Command.register(plugin: Plugin = KotlinBukkitAPI.INSTANCE) {
    serverCommands.register(plugin.name, this)

    val cmds = CommandsRegisterController.commands.get(plugin.name) ?: mutableListOf()
    cmds.add(this)
    CommandsRegisterController.commands.put(plugin.name, cmds)
}

fun Command.unregister() {
    try {
        val knownCommands = knownCommandsField.get(serverCommands) as MutableMap<String, Command>
        val toRemove = ArrayList<String>()
        for ((key, value) in knownCommands) {
            if (value === this) {
                toRemove.add(key)
            }
        }
        for (str in toRemove) {
            knownCommands.remove(str)
        }

        CommandsRegisterController.commands.values.forEach {
            it.removeIf { this === it }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}