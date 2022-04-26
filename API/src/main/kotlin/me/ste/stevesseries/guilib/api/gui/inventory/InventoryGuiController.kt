package me.ste.stevesseries.guilib.api.gui.inventory

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.gui.GuiController
import me.ste.stevesseries.guilib.api.gui.WindowGui
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

abstract class InventoryGuiController : GuiController, WindowGui {
    // Internal managed values
    lateinit var player: Player
    lateinit var manager: GuiManager
    lateinit var inventory: Inventory

    // Abstract methods
    /**
     * Called when a new instance of inventory is needed
     */
    abstract fun createInventory(): Inventory

    /**
     * Called when the inventory contents need to be refreshed
     */
    abstract fun update()

    /**
     * Called when an InventoryClickEvent is fired. The event is NOT automatically cancelled.
     */
    abstract fun onClick(event: InventoryClickEvent)

    /**
     * Called when an InventoryDragEvent is fired. The event is NOT automatically cancelled.
     */
    abstract fun onDrag(event: InventoryDragEvent)

    // Overridable methods
    /**
     * Opens the inventory for the player.
     */
    open fun openInventory() {
        try {
            this.inventory = this.createInventory()
            this.update()
        } catch (t: Throwable) {
            t.printStackTrace()
            this.manager.resetGui(this.player)
            return
        }

        this.player.openInventory(this.inventory)
    }

    // Utility methods
    /**
     * Closes the GUI.
     */
    fun close() {
        this.manager.closeGui(this.player)
    }

    /**
     * Opens a new GUI.
     */
    fun open(controller: GuiController) {
        this.manager.openGui(this.player, controller)
    }

    /**
     * Reopens the GUI.
     */
    fun reopen() {
        this.open(this)
    }

    // Event methods
    /**
     * Called when the GUI is open.
     */
    open fun create() {}

    /**
     * Called when the GUI is closed.
     */
    open fun destroy() {}

    // Internal logic
    /**
     * Called when an [org.bukkit.event.inventory.InventoryCloseEvent] is called.
     */
    fun onClose() {
        try {
            this.destroy()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        this.manager.resetGui(this.player)
    }

    override fun openGui(manager: GuiManager, player: Player) {
        this.player = player
        this.manager = manager

        try {
            this.create()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        this.openInventory()
    }

    @Deprecated(message = "Do not call this method directly! If you wish to close the GUI, use this.close().", replaceWith = ReplaceWith("close()"))
    override fun closeGui(next: GuiController?) {
        if (next !is WindowGui) {
            this.player.closeInventory()
        } else {
            this.onClose()
        }
    }
}