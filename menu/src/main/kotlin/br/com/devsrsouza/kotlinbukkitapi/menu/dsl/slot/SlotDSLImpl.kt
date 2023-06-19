package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.MenuDSL
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

public fun MenuDSL.newSlot(item: ItemStack?, builder: SlotDSL.() -> Unit): SlotDSL {
    return SlotDSLImpl(item, cancelOnClick).apply(builder)
}

public class SlotDSLImpl(
        override val item: ItemStack?,
        override var cancel: Boolean,
        override val eventHandler: SlotEventHandlerDSL = SlotEventHandlerDSL()
) : SlotDSL {

    override val slotData: WeakHashMap<String, Any> = WeakHashMap()
    override val playerSlotData: WeakHashMap<Player, WeakHashMap<String, Any>> = WeakHashMap()

    override fun clone(item: ItemStack?): SlotDSL = SlotDSLImpl(item, cancel, eventHandler.clone())

}