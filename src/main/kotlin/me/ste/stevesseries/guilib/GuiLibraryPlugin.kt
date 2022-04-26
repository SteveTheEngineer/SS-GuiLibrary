package me.ste.stevesseries.guilib

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.extensions.openGui
import me.ste.stevesseries.guilib.listener.InventoryListener
import me.ste.stevesseries.guilib.listener.PlayerListener
import me.ste.stevesseries.guilib.task.GuiTickerTask
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class GuiLibraryPlugin : JavaPlugin() {
    private lateinit var manager: GuiManager

    override fun onEnable() {
        this.manager = GuiManagerImpl()
        GuiManager.setInstance(this.manager)

        this.server.pluginManager.registerEvents(InventoryListener(this.manager), this)
        this.server.pluginManager.registerEvents(PlayerListener(this.manager), this)

        this.server.scheduler.runTaskTimer(this, GuiTickerTask(this.manager), 0L,  0L)
    }

    override fun onDisable() {
        // Close all open GUIs. We are not able to save their state.
        for (uuid in this.manager.getOpenGuis().keys) {
            val player = this.server.getPlayer(uuid) ?: continue
            this.manager.closeGui(player)
        }
    }
}