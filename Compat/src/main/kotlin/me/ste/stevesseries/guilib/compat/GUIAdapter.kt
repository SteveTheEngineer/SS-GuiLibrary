package me.ste.stevesseries.guilib.compat

import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import me.ste.stevesseries.inventoryguilibrary.GUI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

class GUIAdapter(
    val gui: GUI
) : InventoryGuiController() {
    override fun createInventory(): Inventory {
        return if (this.gui.getInventoryType() == InventoryType.CHEST) {
            Bukkit.createInventory(null, gui.getSize(), gui.getTitle());
        } else {
            Bukkit.createInventory(null, gui.getInventoryType(), gui.getTitle());
        }
    }

    override fun update() {
        this.gui.updateInventory(this.inventory)
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.clickedInventory == this.inventory || event.isShiftClick) {
            event.isCancelled = true
        }

        if (event.click == ClickType.DOUBLE_CLICK && event.cursor != null && this.inventory.contains(event.cursor)) {
            event.isCancelled = true
        }

        if (event.clickedInventory != this.inventory) {
            return
        }

        this.gui.handleClick(event.currentItem, event.click, event.slot, this.inventory)
    }

    override fun onDrag(event: InventoryDragEvent) {
        if (!event.rawSlots.any { it < this.inventory.size }) {
            return
        }

        event.isCancelled = true
    }

    override fun create() {
        this.gui.handleOpening(this.createInventory()) // Have to create a duplicate inventory
    }

    override fun destroy() {
        this.gui.handleClosing(this.inventory)
    }
}