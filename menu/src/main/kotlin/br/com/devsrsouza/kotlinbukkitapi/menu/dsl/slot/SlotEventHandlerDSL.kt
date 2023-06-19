package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotInteract
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotMoveTo
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotRender
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotUpdate
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.SlotEventHandler

public typealias MenuPlayerSlotInteractEvent = MenuPlayerSlotInteract.() -> Unit
public typealias MenuPlayerSlotRenderEvent = MenuPlayerSlotRender.() -> Unit
public typealias MenuPlayerSlotUpdateEvent = MenuPlayerSlotUpdate.() -> Unit
public typealias MenuPlayerSlotMoveToEvent = MenuPlayerSlotMoveTo.() -> Unit

public class SlotEventHandlerDSL : SlotEventHandler {

    internal val interactCallbacks = mutableListOf<MenuPlayerSlotInteractEvent>()
    internal val renderCallbacks = mutableListOf<MenuPlayerSlotRenderEvent>()
    internal val updateCallbacks = mutableListOf<MenuPlayerSlotUpdateEvent>()
    internal val moveToSlotCallbacks = mutableListOf<MenuPlayerSlotMoveToEvent>()

    override fun interact(interact: MenuPlayerSlotInteract) {
        for (callback in interactCallbacks) {
            callback(interact)
        }
    }

    override fun render(render: MenuPlayerSlotRender) {
        for (callback in renderCallbacks) {
            callback(render)
        }
    }

    override fun update(update: MenuPlayerSlotUpdate) {
        for (callback in updateCallbacks) {
            callback(update)
        }
    }

    override fun moveToSlot(moveToSlot: MenuPlayerSlotMoveTo) {
        for (callback in moveToSlotCallbacks) {
            callback(moveToSlot)
        }
    }

    override fun clone(): SlotEventHandlerDSL {
        return SlotEventHandlerDSL().also {
            it.interactCallbacks.addAll(interactCallbacks)
            it.renderCallbacks.addAll(renderCallbacks)
            it.updateCallbacks.addAll(updateCallbacks)
            it.moveToSlotCallbacks.addAll(moveToSlotCallbacks)
        }
    }
}
