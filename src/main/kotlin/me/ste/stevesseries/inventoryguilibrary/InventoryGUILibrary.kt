package me.ste.stevesseries.inventoryguilibrary

import me.ste.stevesseries.inventoryguilibrary.listener.InventoryListener
import org.bukkit.plugin.java.JavaPlugin

class InventoryGUILibrary : JavaPlugin() {
    companion object {
        lateinit var PLUGIN: InventoryGUILibrary
    }

    override fun onLoad() {
        PLUGIN = this
    }

    override fun onEnable() {
        this.server.pluginManager.registerEvents(InventoryListener, this)
    }

    override fun onDisable() {
        for ((uuid, _) in PlayerGUIS) {
            server.getPlayer(uuid)?.closeInventory()
        }
    }
}