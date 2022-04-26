package me.ste.stevesseries.guilib.listener

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class InventoryListener(private val manager: GuiManager) : Listener {
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val gui = this.manager.getGui(player) as? InventoryGuiController ?: return

        gui.onClose()
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val gui = this.manager.getGui(player) as? InventoryGuiController ?: return

        gui.onClick(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        val gui = this.manager.getGui(player) as? InventoryGuiController ?: return

        gui.onDrag(event)
    }
}