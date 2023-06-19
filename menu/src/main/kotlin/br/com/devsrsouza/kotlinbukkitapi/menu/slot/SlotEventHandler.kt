package br.com.devsrsouza.kotlinbukkitapi.menu.slot

public interface SlotEventHandler {

    public fun interact(interact: MenuPlayerSlotInteract)

    public fun render(render: MenuPlayerSlotRender)

    public fun update(update: MenuPlayerSlotUpdate)

    public fun moveToSlot(moveToSlot: MenuPlayerSlotMoveTo)

    public fun clone(): SlotEventHandler

}