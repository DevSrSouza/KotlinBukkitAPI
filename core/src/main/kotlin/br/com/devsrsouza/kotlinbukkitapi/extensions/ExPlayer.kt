package br.com.devsrsouza.kotlinbukkitapi.extensions.player

import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.clearArmor() {
    armorContents = arrayOf<ItemStack?>(null, null, null, null)
}

fun PlayerInventory.clearAll() {
    clear()
    clearArmor()
}

val Player.hasItemInHand get() = itemInHand != null && itemInHand.type != Material.AIR

fun Player.playSound(sound: Sound, volume: Float, pitch: Float) = playSound(location, sound, volume, pitch)
fun Player.playNote(instrument: Instrument, note: Note) = playNote(location, instrument, note)