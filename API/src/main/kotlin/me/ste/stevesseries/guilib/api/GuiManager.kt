package me.ste.stevesseries.guilib.api

import me.ste.stevesseries.guilib.api.component.GuiComponent
import me.ste.stevesseries.guilib.api.gui.GuiController
import me.ste.stevesseries.guilib.api.gui.inventory.ComponentGui
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

interface GuiManager {
    companion object {
        private var manager: GuiManager? = null

        fun getInstance() =
            this.manager ?: throw IllegalStateException("The GUI manager has not yet been initialized!")

        fun setInstance(manager: GuiManager) {
            if (this.manager != null) {
                throw IllegalStateException("A GUI manager instance has already been set!")
            }
            this.manager = manager
        }
    }

    fun getPlugin(): Plugin

    /**
     * Opens the GUI for the provided [player].
     */
    fun openGui(player: Player, controller: GuiController)

    /**
     * Opens the component as a [me.ste.stevesseries.guilib.api.gui.inventory.ComponentGui].
     */
    fun openGui(player: Player, component: GuiComponent) {
        this.openGui(player, ComponentGui(component))
    }

    /**
     * Calls the GUI close logic of the implementation.
     * The implementation MUST then reset the gui state using [resetGui].
     */
    fun closeGui(player: Player)

    /**
     * Called to reset the GUI state of a [player] (force close).
     * This method SHOULD ONLY be used by GUI implementations, and not by the users of the said implementations.
     * If you wish to close the GUI as a user, use [closeGui] instead.
     */
    fun resetGui(player: Player)

    /**
     * Gets the currently open GUI of the [player].
     */
    fun getGui(player: Player): GuiController?

    /**
     * Gets all currently open GUIs mapped by their players.
     */
    fun getOpenGuis(): Map<UUID, GuiController>
}