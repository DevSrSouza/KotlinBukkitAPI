package br.com.devsrsouza.kotlinbukkitapi.menu

public interface MenuEventHandler {

    public fun update(update: MenuPlayerUpdate)

    public fun close(close: MenuPlayerClose)

    public fun moveToMenu(moveToMenu: MenuPlayerMoveTo)

    public fun preOpen(preOpen: MenuPlayerPreOpen)

    public fun open(open: MenuPlayerOpen)
}
