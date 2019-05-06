package br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot

import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.MenuDSL
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun MenuDSL.newSlot(item: ItemStack, builder: SlotDSL.() -> Unit): SlotDSL {
    return SlotDSLImpl(item, cancel).apply(builder)
}

class SlotDSLImpl(
        override val item: ItemStack?,
        override var cancel: Boolean,
        override val eventHandler: SlotEventHandlerDSL = SlotEventHandlerDSL()
) : SlotDSL {

    override val slotData = mutableMapOf<String, Any>()
    override val playerSlotData = mutableMapOf<Player, MutableMap<String, Any>>()

    override fun clone(item: ItemStack?): SlotDSL = SlotDSLImpl(item, cancel, eventHandler.clone())

}