package br.com.devsrsouza.kotlinbukkitapi.extensions.server

import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.server
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.messaging.Messenger
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scoreboard.ScoreboardManager
import java.io.File
import java.util.*

val isPrimaryThread get() = server.isPrimaryThread

fun offlinePlayer(uuid: UUID): OfflinePlayer = server.getOfflinePlayer(uuid)
fun offlinePlayer(name: String): OfflinePlayer = server.getOfflinePlayer(name)

fun player(uuid: UUID): Player? = server.getPlayer(uuid)
fun player(name: String): Player? = server.getPlayer(name)
fun playerExact(name: String): Player? = server.getPlayerExact(name)
fun matchPlayer(name: String): List<Player> = server.matchPlayer(name)

fun onlinePlayer(uuid: UUID): Player? = server.getPlayer(uuid)
fun onlinePlayer(name: String): Player? = server.getPlayerExact(name)

val onlinePlayers: Collection<Player> get() = server.onlinePlayers
val worldType: String get() = server.worldType
val generateStructures: Boolean get() = server.generateStructures
val allowEnd: Boolean get() = server.allowEnd
val allowNether: Boolean get() = server.allowNether
val allowFlight: Boolean get() = server.allowFlight
val whitelistedPlayers: MutableSet<OfflinePlayer> get() = server.whitelistedPlayers
val updateFolder: String get() = server.updateFolder
val updateFolderFile: File get() = server.updateFolderFile
val connectionThrottle: Long get() = server.connectionThrottle
val recipes: Iterator<Recipe> get() = server.recipeIterator()
val ticksPerAnimalSpawns: Int get() = server.ticksPerAnimalSpawns
val ticksPerMonsterSpawns: Int get() = server.ticksPerMonsterSpawns
val pluginManager: PluginManager get() = server.pluginManager
val scheduler: BukkitScheduler get() = server.scheduler
val servicesManager: ServicesManager get() = server.servicesManager
val worlds: List<World> get() = server.worlds
val onlineMode: Boolean get() = server.onlineMode
val isHardcore: Boolean get() = server.isHardcore
val bannedPlayers: Set<OfflinePlayer> get() = server.bannedPlayers
val ipBans: Set<String> get() = server.ipBans
val operators: Set<OfflinePlayer> get() = server.operators
val worldContainer: File get() = server.worldContainer
val messenger: Messenger get() = server.messenger
val monsterSpawnLimit: Int get() = server.monsterSpawnLimit
val animalSpawnLimit: Int get() = server.animalSpawnLimit
val ambientSpawnLimit: Int get() = server.ambientSpawnLimit
val scoreboardManager: ScoreboardManager get() = server.scoreboardManager

var idleTimeout: Int
    get() = server.idleTimeout
    set(value) { server.idleTimeout = value }

var defaultGameMode: GameMode
    get() = server.defaultGameMode
    set(value) { server.defaultGameMode = value }

var spawnRadius: Int
    get() = server.spawnRadius
    set(value) { server.spawnRadius = value }

var whitelist: Boolean
    get() = server.hasWhitelist()
    set(value) { server.setWhitelist(value) }

fun reloadWhitelist() = server.reloadWhitelist()

fun world(name: String): World? = server.getWorld(name)

fun addRecipe(recipe: Recipe): Boolean = server.addRecipe(recipe)
fun recipesFor(item: ItemStack): List<Recipe> = server.getRecipesFor(item)

fun WorldCreator.create(): World = server.createWorld(this)