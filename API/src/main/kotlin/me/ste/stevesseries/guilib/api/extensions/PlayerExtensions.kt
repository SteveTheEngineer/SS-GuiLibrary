package me.ste.stevesseries.guilib.api.extensions

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.component.GuiComponent
import me.ste.stevesseries.guilib.api.gui.GuiController
import me.ste.stevesseries.guilib.api.gui.inventory.ComponentGui
import org.bukkit.entity.Player

/**
 * Shortcut for [GuiManager].getInstance().openGui(player, controller).
 */
fun Player.openGui(controller: GuiController) {
    GuiManager.getInstance().openGui(this, controller)
}

/**
 * Shortcut for [GuiManager].getInstance().closeGui(player).
 */
fun Player.closeGui() {
    GuiManager.getInstance().closeGui(this)
}

/**
 * Shortcut for [GuiManager].getInstance().getGui(player).
 */
fun Player.getGui() = GuiManager.getInstance().getGui(this)

/**
 * Shortcut for openGui(ComponentGui(component)).
 */
fun Player.openGui(component: GuiComponent) {
    this.openGui(ComponentGui(component))
}
