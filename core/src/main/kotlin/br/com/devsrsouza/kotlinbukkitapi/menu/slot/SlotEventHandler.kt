package br.com.devsrsouza.kotlinbukkitapi.menu.slot

interface SlotEventHandler {

    fun interact(interact: MenuPlayerSlotInteract)

    fun render(render: MenuPlayerSlotRender)

    fun update(update: MenuPlayerSlotUpdate)

    fun moveToSlot(moveToSlot: MenuPlayerSlotMoveTo)

    fun clone(): SlotEventHandler

}