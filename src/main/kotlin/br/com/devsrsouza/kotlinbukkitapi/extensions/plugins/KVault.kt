package br.com.devsrsouza.kotlinbukkitapi.extensions.plugins.vault

import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

val hasVault by lazy { Bukkit.getServer().pluginManager.getPlugin("Vault") != null }
val economy by lazy { Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)?.provider }
val chat by lazy { Bukkit.getServer().servicesManager.getRegistration(Chat::class.java)?.provider }
val permission by lazy { Bukkit.getServer().servicesManager.getRegistration(Permission::class.java)?.provider }

val Player.vault get() = Vault(this)
val OfflinePlayer.vault get() = VaultOffline(this)

open class VaultOffline(private val player: OfflinePlayer) {
    val economy = br.com.devsrsouza.kotlinbukkitapi.extensions.plugins.vault.Economy(player)
    //val PERMISSION = br.com.devsrsouza.kotlinbukkitapi.extensions.plugins.vault.Permission(player)
}
class Vault(player: Player) : VaultOffline(player){
    val chat = br.com.devsrsouza.kotlinbukkitapi.extensions.plugins.vault.Chat(player)
}

class Economy(private val player: OfflinePlayer) {
    fun getBalance() = economy!!.getBalance(player)
    fun hasAccount() = economy!!.hasAccount(player)
    fun has(amount: Double) = economy!!.has(player, amount)
    fun withdraw(amount: Double) = economy!!.withdrawPlayer(player, amount)
    fun deposit(amount: Double) = economy!!.depositPlayer(player, amount)
}
class Chat(private val player: Player) {
    fun getPrefix() = chat!!.getPlayerPrefix(player)
    fun setPrefix(prefix: String) = chat!!.setPlayerPrefix(player, prefix)
    fun getSuffix() = chat!!.getPlayerSuffix(player)
    fun setSuffix(suffix: String) = chat!!.setPlayerSuffix(player, suffix)
    fun inGroup(group: String) = chat!!.playerInGroup(player, group)
    fun getGroups() = chat!!.getPlayerGroups(player)
    fun getPrimaryGroup() = chat!!.getPrimaryGroup(player)
}
/*
class Permission(private val player: Player) {

}*/
