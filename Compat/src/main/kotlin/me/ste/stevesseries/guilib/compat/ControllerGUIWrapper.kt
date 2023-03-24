package me.ste.stevesseries.guilib.compat

import me.ste.stevesseries.guilib.api.gui.GuiController
import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import me.ste.stevesseries.inventoryguilibrary.GUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ControllerGUIWrapper(
    private val playerObj: Player,
    private val controller: GuiController
) : GUI(
    playerObj,
    (controller as? InventoryGuiController)?.inventory?.type ?: InventoryType.CHEST,
    (controller as? InventoryGuiController)?.inventory?.size ?: 0,
    "ControllerGUIWrapper"
) {
    override fun updateInventory(inventory: Inventory?) {
        (this.controller as? InventoryGuiController)?.update()
    }

    override fun handleClick(stack: ItemStack?, clickType: ClickType, slot: Int, inventory: Inventory) {
        if (clickType == ClickType.NUMBER_KEY) {
            return // Better just to ignore
        }

        (this.controller as? InventoryGuiController)?.onClick(
            InventoryClickEvent(
                this.playerObj.openInventory,
                InventoryType.SlotType.CONTAINER,
                slot,
                clickType,
                InventoryAction.NOTHING
            )
        )
    }

    override fun handleOpening(inventory: Inventory?) {
        (this.controller as? InventoryGuiController)?.create()
    }

    override fun handleClosing(inventory: Inventory?) {
        (this.controller as? InventoryGuiController)?.destroy()
    }
}