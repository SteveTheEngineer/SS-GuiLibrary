package me.ste.stevesseries.guilib.compat

import me.ste.stevesseries.guilib.api.gui.GuiController
import me.ste.stevesseries.guilib.api.gui.inventory.GridInventorySize
import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.inventory.Position2I
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ControllerWidgetWrapper(private val gui: GuiController) : Widget(0, 0, 0, 0) {
    init {
        if (this.gui is InventoryGuiController) {
            val size = GridInventorySize.fromInventoryType(this.gui.inventory.type, this.gui.inventory.size)

            if (size != null) {
                this.width = size.width
                this.height = size.height
            }
        }
    }

    override fun render(canvas: ItemCanvas) {
        if (this.gui is InventoryGuiController) {
            this.gui.update()

            for ((index, stack) in this.gui.inventory.withIndex()) {
                val x = index % this.width
                val y = index / this.width

                canvas[x, y] = stack
            }
        }
    }

    override fun createInventory(): Inventory? {
        return (this.gui as? InventoryGuiController)?.createInventory()
    }

    override fun click(x: Int, y: Int, type: ClickType) {
        (this.gui as? InventoryGuiController)?.onClick(
            InventoryClickEvent(
                this.player.openInventory,
                InventoryType.SlotType.CONTAINER,
                y * this.width + x,
                type,
                InventoryAction.NOTHING
            )
        )
    }

    override fun numberKey(x: Int, y: Int, hotbarButton: Int) {
        (this.gui as? InventoryGuiController)?.onClick(
            InventoryClickEvent(
                this.player.openInventory,
                InventoryType.SlotType.CONTAINER,
                y * this.width + x,
                ClickType.NUMBER_KEY,
                InventoryAction.NOTHING,
                hotbarButton
            )
        )
    }

    override fun drag(newItems: Map<Position2I, ItemStack>, newCursor: ItemStack?, type: DragType) {
        (this.gui as? InventoryGuiController)?.onDrag(
            InventoryDragEvent(
                this.player.openInventory,
                newCursor,
                this.player.itemOnCursor,
                type == DragType.SINGLE,
                newItems.mapKeys { (key, _) -> key.y * this.width + key.x }
            )
        )
    }

    override fun create() {
        (this.gui as? InventoryGuiController)?.create()
    }

    override fun destroy() {
        (this.gui as? InventoryGuiController)?.destroy()
    }
}