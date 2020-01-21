package br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.slot

import br.com.devsrsouza.kotlinbukkitapi.collections.ObservableList
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.MenuDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.ItemsAdapter
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.MenuPaginationImpl
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.Orientation
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotDSL
import org.bukkit.entity.Player
import java.lang.IllegalArgumentException
import java.util.*

private const val CURRENT_PAGE_KEY = "PAGINATION:CURRENT_PAGE"

inline fun <T> MenuDSL.pagination(
        items: ObservableList<T>,
        nextPageSlot: SlotDSL,
        previousPageSlot: SlotDSL,
        startLine: Int = 1,
        endLine: Int = lines-1,
        startSlot: Int = 1,
        endSlot: Int = 9,
        orientation: Orientation = Orientation.HORIZONTAL,
        noinline itemsAdapterOnOpen: ItemsAdapter<T>? = null,
        noinline itemsAdapterOnUpdate: ItemsAdapter<T>? = null,
        builder: MenuPaginationImpl<T>.() -> Unit
): MenuPaginationImpl<T> {
    if(startSlot > endSlot) throw IllegalArgumentException()
    if(startLine > endLine) throw IllegalArgumentException()

    return MenuPaginationImpl(
            this,
            items,
            nextPageSlot,
            previousPageSlot,
            startLine,
            endLine,
            startSlot,
            endSlot,
            orientation,
            itemsAdapterOnOpen,
            itemsAdapterOnUpdate
    ).apply(builder)
}

interface PaginationSlotDSL<T> {
    val slotRoot: SlotDSL

    val paginationEventHandler: PaginationSlotEventHandler<T>

    val slotData: WeakHashMap<String, Any>
    val playerSlotData: WeakHashMap<Player, WeakHashMap<String, Any>>

    /**
     * Cancel the interaction with this slot
     */
    var cancel: Boolean

    fun onPageChange(pageChange: MenuPlayerSlotPageChangeEvent<T>) {
        paginationEventHandler.pageChangeCallbacks.add(pageChange)
    }

    fun onClick(click: MenuPlayerPageSlotInteractEvent<T>) {
        paginationEventHandler.interactCallbacks.add(click)
    }

    fun onRender(render: MenuPlayerPageSlotRenderEvent<T>) {
        paginationEventHandler.renderCallbacks.add(render)
    }

    fun onUpdate(update: MenuPlayerPageSlotUpdateEvent<T>) {
        paginationEventHandler.updateCallbacks.add(update)
    }
}

