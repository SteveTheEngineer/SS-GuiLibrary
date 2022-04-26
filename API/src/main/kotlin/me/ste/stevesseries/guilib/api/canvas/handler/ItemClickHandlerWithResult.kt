package me.ste.stevesseries.guilib.api.canvas.handler

import org.bukkit.event.inventory.ClickType

fun interface ItemClickHandlerWithResult {
    /**
     * @return true, if the click was processed, and other handlers are not to be called
     */
    fun handle(x: Int, y: Int, type: ClickType, numberKey: Int): Boolean
}