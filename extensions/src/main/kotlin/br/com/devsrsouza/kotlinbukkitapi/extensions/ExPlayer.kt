package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

public fun PlayerInventory.clearArmor() {
    armorContents = arrayOf<ItemStack?>(null, null, null, null)
}

public fun PlayerInventory.clearAll() {
    clear()
    clearArmor()
}

public val Player.hasItemInHand: Boolean get() = itemInHand != null && itemInHand.type != Material.AIR

public fun Player.playSound(sound: Sound, volume: Float, pitch: Float): Unit = playSound(location, sound, volume, pitch)
public fun Player.playNote(instrument: Instrument, note: Note): Unit = playNote(location, instrument, note)
public fun <T> Player.playEffect(effect: Effect, data: T? = null): Unit = playEffect(location, effect, data)

public fun CommandSender.msg(message: List<String>): Unit = message.forEach { msg(it) }

public fun Player.resetWalkSpeed() {
    walkSpeed = 0.2f
}

public fun Player.resetFlySpeed() {
    flySpeed = 0.1f
}