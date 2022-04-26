package me.ste.stevesseries.guilib.listener

import me.ste.stevesseries.guilib.api.GuiManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener(private val manager: GuiManager) : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        this.manager.closeGui(player)
    }
}