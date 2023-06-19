package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot.SlotDSL
import org.bukkit.entity.Player
import java.util.*

public interface PaginationSlotDSL<T> {
    public val slotRoot: SlotDSL

    public val paginationEventHandler: PaginationSlotEventHandler<T>

    public val slotData: WeakHashMap<String, Any>
    public val playerSlotData: WeakHashMap<Player, WeakHashMap<String, Any>>

    /**
     * Cancel the interaction with this slot
     */
    public var cancel: Boolean

    public fun onPageChange(pageChange: MenuPlayerSlotPageChangeEvent<T>) {
        paginationEventHandler.pageChangeCallbacks.add(pageChange)
    }

    public fun onClick(click: MenuPlayerPageSlotInteractEvent<T>) {
        paginationEventHandler.interactCallbacks.add(click)
    }

    public fun onRender(render: MenuPlayerPageSlotRenderEvent<T>) {
        paginationEventHandler.renderCallbacks.add(render)
    }

    public fun onUpdate(update: MenuPlayerPageSlotUpdateEvent<T>) {
        paginationEventHandler.updateCallbacks.add(update)
    }
}
