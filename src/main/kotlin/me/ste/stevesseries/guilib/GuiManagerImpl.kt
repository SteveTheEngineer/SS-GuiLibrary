package me.ste.stevesseries.guilib

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.gui.GuiController
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class GuiManagerImpl(
    private val plugin: Plugin
) : GuiManager {
    private val guiMap = mutableMapOf<UUID, GuiController>()

    override fun getPlugin() = this.plugin

    override fun openGui(player: Player, controller: GuiController) {
        val gui = this.guiMap[player.uniqueId]
        gui?.closeGui(controller)

        controller.openGui(this, player)
        this.guiMap[player.uniqueId] = controller
    }

    override fun closeGui(player: Player) {
        val gui = this.guiMap[player.uniqueId] ?: return
        gui.closeGui(null)
    }

    override fun resetGui(player: Player) {
        this.guiMap -= player.uniqueId
    }

    override fun getGui(player: Player) = this.guiMap[player.uniqueId]
    override fun getOpenGuis() = this.guiMap.toMap()
}