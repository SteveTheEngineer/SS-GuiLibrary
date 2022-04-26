package me.ste.stevesseries.guilib.api.util

/**
 * If a [me.ste.stevesseries.guilib.api.gui.GuiController] implements this interface, the [tick] method will be called every tick the GUI is open.
 */
interface Ticking {
    fun tick()
}