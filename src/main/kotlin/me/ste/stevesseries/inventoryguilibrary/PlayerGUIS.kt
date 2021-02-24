package me.ste.stevesseries.inventoryguilibrary

import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.entity.Player
import java.util.*

/**
 * This class manages the GUIs. If you need to open a GUI for a player, this is the class that does that
 */
object PlayerGUIS : Iterable<MutableMap.MutableEntry<UUID, Widget>> {
    /**
     * Open a Steve's Series | Inventory GUI Library GUI
     * @param gui GUI to open
     */
    fun Player.openGui(gui: Widget) {
        PlayerGUIS[this] = gui
    }

    /**
     * Get the currently open Steve's Series | Inventory GUI Library GUI
     * @return the gui
     */
    fun Player.getGui(): Widget? = PlayerGUIS[this]

    /**
     * Re-render the currently open GUI
     */
    fun Player.rerenderGui() {
        rerender(this)
    }

    /**
     * All currently open GUIs. The key is the player unique id
     */
    val GUIS: MutableMap<UUID, Widget> = HashMap()

    /**
     * Open a GUI for the given player
     * @param player player
     * @param gui GUI to open
     */
    operator fun set(player: Player, gui: Widget) {
        val inventory = gui.createInventory()
            ?: throw IllegalArgumentException("The widget does not support being opened as an inventory")

        val canvas = ItemCanvas(gui.width, gui.height)
        gui.recurse {
            it.player = player
            it.create()

            val widgetCanvas = ItemCanvas(it.width, it.height)
            it.render(widgetCanvas)
            widgetCanvas.drawOn(canvas, it.rootX, it.rootY)
        }

        player.closeInventory()
        canvas.drawOn(inventory, 0, 0)
        player.openInventory(inventory)

        this.GUIS[player.uniqueId] = gui
    }

    /**
     * Get the currently open GUI of the player
     * @param player target player
     * @return the gui
     */
    operator fun get(player: Player) = this.GUIS[player.uniqueId]

    /**
     * Re-render the currently open GUI of the player
     * @param player target player
     */
    fun rerender(player: Player) {
        val gui: Widget? = this[player]
        if (gui != null) {
            val canvas = ItemCanvas(gui.width, gui.height)
            gui.recurse {
                val widgetCanvas = ItemCanvas(it.width, it.height)
                it.render(widgetCanvas)
                widgetCanvas.drawOn(canvas, it.rootX, it.rootY)
            }
            canvas.drawOn(player.openInventory.topInventory, 0, 0)
        }
    }

    override fun iterator(): Iterator<MutableMap.MutableEntry<UUID, Widget>> = this.GUIS.iterator()
}