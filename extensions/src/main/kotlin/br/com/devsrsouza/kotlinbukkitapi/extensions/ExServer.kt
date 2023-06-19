package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.WorldCreator
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

public val isPrimaryThread: Boolean get() = server.isPrimaryThread

public fun offlinePlayer(uuid: UUID): OfflinePlayer = server.getOfflinePlayer(uuid)
public fun offlinePlayer(name: String): OfflinePlayer = server.getOfflinePlayer(name)

public fun player(uuid: UUID): Player? = server.getPlayer(uuid)
public fun player(name: String): Player? = server.getPlayer(name)
public fun playerExact(name: String): Player? = server.getPlayerExact(name)
public fun matchPlayer(name: String): List<Player> = server.matchPlayer(name)

public fun onlinePlayer(uuid: UUID): Player? = server.getPlayer(uuid)
public fun onlinePlayer(name: String): Player? = server.getPlayerExact(name)

public val onlinePlayers: Collection<Player> get() = server.onlinePlayers
public val worldType: String get() = server.worldType
public val generateStructures: Boolean get() = server.generateStructures
public val allowEnd: Boolean get() = server.allowEnd
public val allowNether: Boolean get() = server.allowNether
public val allowFlight: Boolean get() = server.allowFlight
public val whitelistedPlayers: MutableSet<OfflinePlayer> get() = server.whitelistedPlayers
public val updateFolder: String get() = server.updateFolder
public val updateFolderFile: File get() = server.updateFolderFile
public val connectionThrottle: Long get() = server.connectionThrottle
public val recipes: Iterator<Recipe> get() = server.recipeIterator()
public val ticksPerAnimalSpawns: Int get() = server.ticksPerAnimalSpawns
public val ticksPerMonsterSpawns: Int get() = server.ticksPerMonsterSpawns
public val pluginManager: PluginManager get() = server.pluginManager
public val scheduler: BukkitScheduler get() = server.scheduler
public val servicesManager: ServicesManager get() = server.servicesManager
public val worlds: List<World> get() = server.worlds
public val onlineMode: Boolean get() = server.onlineMode
public val isHardcore: Boolean get() = server.isHardcore
public val bannedPlayers: Set<OfflinePlayer> get() = server.bannedPlayers
public val ipBans: Set<String> get() = server.ipBans
public val operators: Set<OfflinePlayer> get() = server.operators
public val worldContainer: File get() = server.worldContainer
public val messenger: Messenger get() = server.messenger
public val monsterSpawnLimit: Int get() = server.monsterSpawnLimit
public val animalSpawnLimit: Int get() = server.animalSpawnLimit
public val ambientSpawnLimit: Int get() = server.ambientSpawnLimit
public val scoreboardManager: ScoreboardManager? get() = server.scoreboardManager

public var idleTimeout: Int
    get() = server.idleTimeout
    set(value) { server.idleTimeout = value }

public var defaultGameMode: GameMode
    get() = server.defaultGameMode
    set(value) { server.defaultGameMode = value }

public var spawnRadius: Int
    get() = server.spawnRadius
    set(value) { server.spawnRadius = value }

public var whitelist: Boolean
    get() = server.hasWhitelist()
    set(value) { server.setWhitelist(value) }

public fun reloadWhitelist(): Unit = server.reloadWhitelist()

public fun world(name: String): World? = server.getWorld(name)

public fun addRecipe(recipe: Recipe): Boolean = server.addRecipe(recipe)
public fun recipesFor(item: ItemStack): List<Recipe> = server.getRecipesFor(item)

public fun WorldCreator.create(): World? = server.createWorld(this)
