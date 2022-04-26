package me.ste.stevesseries.guilib.api.component

import me.ste.stevesseries.guilib.api.gui.inventory.GridInventorySize
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * This class should be implemented by all components that are intended to be used directly in a GUI.
 */
interface RootComponent {
    /**
     * @return the size of the GUI.
     */
    fun getSize(): GridInventorySize

    /**
     * @return the title of the GUI.
     */
    fun getTitle(): String

    /**
     * Same as [me.ste.stevesseries.guilib.api.gui.inventory.GridInventoryGui.click].
     */
    fun click(x: Int, y: Int, type: ClickType, numberKey: Int) = false
}