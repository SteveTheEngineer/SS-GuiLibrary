package me.ste.stevesseries.guilib.api.canvas.handler

import org.bukkit.event.inventory.ClickType

fun interface ItemClickHandler {
    fun handle(x: Int, y: Int, type: ClickType, numberKey: Int)
}