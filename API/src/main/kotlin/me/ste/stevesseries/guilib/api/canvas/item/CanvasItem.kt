package me.ste.stevesseries.guilib.api.canvas.item

import me.ste.stevesseries.guilib.api.canvas.handler.ItemClickHandler
import org.bukkit.inventory.ItemStack

data class CanvasItem(
    val stack: ItemStack?,
    val clickHandler: ItemClickHandler
)