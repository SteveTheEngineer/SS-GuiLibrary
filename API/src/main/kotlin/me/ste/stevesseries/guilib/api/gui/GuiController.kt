package me.ste.stevesseries.guilib.api.gui

import me.ste.stevesseries.guilib.api.GuiManager
import org.bukkit.entity.Player

interface GuiController {
    /**
     * Called when the GUI is open using [GuiManager.openGui]
     */
    fun openGui(manager: GuiManager, player: Player)

    /**
     * Called when the GUI is closed using [GuiManager.closeGui].
     * Implementations MUST call [GuiManager.resetGui] after the GUI has been closed.
     */
    fun closeGui(next: GuiController?)
}