package me.ste.stevesseries.guilib.api.gui.inventory

import me.ste.stevesseries.guilib.api.canvas.ItemCanvas
import me.ste.stevesseries.guilib.api.canvas.handler.ItemClickHandler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.DragType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

/**
 * A gui that is rendered using an [ItemCanvas].
 */
abstract class GridInventoryGui : InventoryGuiController() {
    // Used to watch state
    /**
     * The current size of the GUI.
     */
    lateinit var currentSize: GridInventorySize
        private set

    /**
     * The current title of the GUI.
     */
    lateinit var currentTitle: String
        private set

    private var clickHandlers = emptyArray<ItemClickHandler?>()

    // Abstracts
    /**
     * @return the size of the inventory.
     */
    abstract fun getSize(): GridInventorySize

    /**
     * @return the title of the inventory.
     */
    abstract fun getTitle(): String

    /**
     * Renders the GUI.
     */
    abstract fun render(canvas: ItemCanvas)

    // Override behavior functions.
    /**
     * Called when a slot is clicked inside the GUI. This is also the only way to process [ClickType.WINDOW_BORDER_LEFT] or [ClickType.WINDOW_BORDER_RIGHT].
     * @return true, if the click was processed, and other handlers are not to be called.
     */
    open fun click(x: Int, y: Int, type: ClickType, numberKey: Int) = false

    // Utility methods
    /**
     * Updates the inventory size, title and contents.
     */
    fun changed() {
        val newSize = this.getSize()
        val newTitle = this.getTitle()

        if (this.currentSize != newSize || this.currentTitle != newTitle) {
            this.reopen()
            return
        }

        this.update()
    }

    // Internal logic
    override fun create() {
        this.currentTitle = this.getTitle()
        this.currentSize = this.getSize()
    }

    @Deprecated(message = "Do not call this method directly! Use changed() instead.", replaceWith = ReplaceWith("changed()"))
    override fun update() {
        this.inventory.clear()

        val canvas = ItemCanvas(this.currentSize.width, this.currentSize.height)
        this.render(canvas)

        val items = canvas.items

        this.clickHandlers = arrayOfNulls(items.size)
        for ((index, item) in items.withIndex()) {
            this.inventory.setItem(index, item?.stack)
            this.clickHandlers[index] = item?.clickHandler
        }
    }

    private fun internalClick(slot: Int, type: ClickType, numberKey: Int) {
        val x = slot % this.currentSize.width
        val y = slot / this.currentSize.width

        if (this.click(x, y, type, numberKey)) {
            return
        }

        val handler = this.clickHandlers[slot] ?: return
        handler.handle(x, y, type, numberKey)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == this.inventory || event.isShiftClick) {
            event.isCancelled = true
        }
        if (event.click == ClickType.DOUBLE_CLICK && event.cursor != null && this.inventory.contains(event.cursor)) {
            event.isCancelled = true
        }

        if (event.clickedInventory != this.inventory) {
            return
        }

        this.internalClick(event.slot, event.click, event.hotbarButton)
    }

    override fun onDrag(event: InventoryDragEvent) {
        if (!event.rawSlots.any { it < this@GridInventoryGui.inventory.size }) {
            return
        }
        event.isCancelled = true

        val slot = event.rawSlots.singleOrNull() ?: return
        val type = when (event.type) {
            DragType.EVEN -> ClickType.LEFT
            DragType.SINGLE -> ClickType.RIGHT
        }

        this.internalClick(slot, type, -1)
    }

    override fun createInventory(): Inventory {
        this.currentTitle = this.getTitle()
        this.currentSize = this.getSize()

        return this.currentSize.createInventory(this.player.server, this.currentTitle)
    }
}