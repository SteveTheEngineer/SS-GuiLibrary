package me.ste.stevesseries.guilib.compat

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import me.ste.stevesseries.inventoryguilibrary.PlayerGUIS
import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.inventory.Position2I
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.DragType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class WidgetAdapter(
    val widget: Widget
) : InventoryGuiController() {
    override fun createInventory() = this.widget.createInventory() ?: throw IllegalArgumentException("The widget does not support being open as an inventory")

    override fun create() {
        this.widget.recurse {
            it.player = player
            it.create()
        }
    }

    override fun update() {
        val canvas = ItemCanvas(this.widget.width, this.widget.height)
        this.widget.recurse {
            it.player = player

            val widgetCanvas = ItemCanvas(it.width, it.height)
            it.render(widgetCanvas)
            widgetCanvas.drawOnIgnoreNull(canvas, it.rootX, it.rootY)
        }

        canvas.drawOn(this.inventory, 0, 0)
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

        val position = ItemCanvas.getPositionByIndex(event.slot, this.widget.width)

        Bukkit.getScheduler().runTask(GuiManager.getInstance().getPlugin()) { ->
            this.widget.recurse {
                if (ItemCanvas.isInside(
                        position.x,
                        position.y,
                        it.rootX,
                        it.rootY,
                        it.width,
                        it.height
                    )
                ) {
                    if (event.click == ClickType.NUMBER_KEY) {
                        it.numberKey(position.x - it.rootX, position.y - it.rootY, event.hotbarButton)
                    } else {
                        it.click(position.x - it.rootX, position.y - it.rootY, event.click)
                    }
                }
            }
        }
    }

    override fun onDrag(event: InventoryDragEvent) {
        if (!event.rawSlots.any { it < this.inventory.size }) {
            return
        }

        event.isCancelled = true

        for ((slot, _) in event.newItems) {
            if (slot >= this.widget.width * this.widget.height - 1) {
                continue
            }

            Bukkit.getScheduler().runTask(GuiManager.getInstance().getPlugin()) { ->
                this.widget.recurse {
                    val newItems: MutableMap<Position2I, ItemStack> = HashMap()
                    for ((slot2, stack) in event.newItems) {
                        val position = ItemCanvas.getPositionByIndex(slot2, this.widget.width)
                        if (ItemCanvas.isInside(
                                position.x,
                                position.y,
                                it.rootX,
                                it.rootY,
                                it.width,
                                it.height
                            )
                        ) {
                            newItems[Position2I(position.x - it.rootX, position.y - it.rootY)] = stack
                        }
                    }
                    if (newItems.isNotEmpty()) {
                        it.drag(newItems, event.cursor, event.type)
                    }
                }
            }
        }
    }

    override fun destroy() {
        this.widget.recurse { it.destroy() }
    }
}