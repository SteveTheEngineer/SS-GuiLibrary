package me.ste.stevesseries.guilib.task

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.util.Ticking

class GuiTickerTask(private val manager: GuiManager) : Runnable {
    override fun run() {
        for (gui in this.manager.getOpenGuis().values) {
            if (gui !is Ticking) {
                continue
            }

            gui.tick()
        }
    }
}