package br.com.devsrsouza.kotlinbukkitapi

import br.com.devsrsouza.kotlinbukkitapi.controllers.*
import br.com.devsrsouza.kotlinbukkitapi.controllers.BungeeCordController
import br.com.devsrsouza.kotlinbukkitapi.controllers.CommandController
import br.com.devsrsouza.kotlinbukkitapi.controllers.MenuController
import br.com.devsrsouza.kotlinbukkitapi.controllers.PlayerController
import br.com.devsrsouza.kotlinbukkitapi.controllers.ProviderController
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.registerEvents
import me.bristermitten.pdm.PDMBuilder
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

internal fun provideKotlinBukkitAPI(): KotlinBukkitAPI {
    return Bukkit.getServer().pluginManager.getPlugin("KotlinBukkitAPI") as KotlinBukkitAPI?
            ?: throw IllegalAccessException("The plugin KotlinBukkitAPI is not loaded yet")
}

private const val BSTATS_PLUGIN_ID = 6356

class KotlinBukkitAPI : JavaPlugin() {
    init { PDMBuilder().build().loadAllDependencies().join() }

    internal val commandController = CommandController(this)
    internal val menuController = MenuController(this)
    internal val playerController = PlayerController(this)
    internal val providerController = ProviderController(this)
    internal val bungeeCordController = BungeeCordController(this)

    internal lateinit var metrics: Metrics private set

    private val controllers = listOf<KBAPIController>(
            commandController, menuController, playerController,
            providerController, bungeeCordController
    )

    override fun onEnable() {
        for (controller in controllers) {
            controller.onEnable()

            if(controller is Listener)
                registerEvents(controller)
        }

        // setup metrics
        metrics = Metrics(this, BSTATS_PLUGIN_ID)
    }
}