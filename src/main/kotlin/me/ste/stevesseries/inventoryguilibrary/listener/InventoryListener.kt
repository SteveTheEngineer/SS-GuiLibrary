package me.ste.stevesseries.inventoryguilibrary.listener

import me.ste.stevesseries.inventoryguilibrary.InventoryGUILibrary
import me.ste.stevesseries.inventoryguilibrary.PlayerGUIS
import me.ste.stevesseries.inventoryguilibrary.inventory.ItemCanvas
import me.ste.stevesseries.inventoryguilibrary.inventory.Position2I
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

object InventoryListener : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val gui = PlayerGUIS[event.player]
        if (gui != null) {
            gui.recurse { it.destroy() }
            PlayerGUIS.GUIS -= event.player.uniqueId
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.player is Player) {
            val player = event.player as Player
            val gui = PlayerGUIS[player]
            if (gui != null) {
                gui.recurse { it.destroy() }
                PlayerGUIS.GUIS -= player.uniqueId
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked is Player) {
            val player = event.whoClicked as Player
            val gui = PlayerGUIS[player]
            if (gui != null && (event.clickedInventory !is PlayerInventory || event.isShiftClick)) {
                event.isCancelled = true
                val position = ItemCanvas.getPositionByIndex(event.slot, gui.width)
                Bukkit.getScheduler().runTask(InventoryGUILibrary.PLUGIN) { ->
                    gui.recurse {
                        if (ItemCanvas.isInside(
                                position.x,
                                position.y,
                                it.rootX,
                                it.rootY,
                                it.width,
                                it.height
                            )
                        ) {
                            if (event.click == ClickType.NUMBER_KEY) {
                                it.numberKey(position.x - it.rootX, position.y - it.rootY, event.hotbarButton)
                            } else {
                                it.click(position.x - it.rootX, position.y - it.rootY, event.click)
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.whoClicked is Player) {
            val player = event.whoClicked as Player
            val gui = PlayerGUIS[player]
            if (gui != null) {
                for ((slot, _) in event.newItems) {
                    if (slot < gui.width * gui.height - 1) {
                        event.isCancelled = true
                        Bukkit.getScheduler().runTask(InventoryGUILibrary.PLUGIN) { ->
                            gui.recurse {
                                val newItems: MutableMap<Position2I, ItemStack> = HashMap()
                                for ((slot2, stack) in event.newItems) {
                                    val position = ItemCanvas.getPositionByIndex(slot2, gui.width)
                                    if (ItemCanvas.isInside(
                                            position.x,
                                            position.y,
                                            it.rootX,
                                            it.rootY,
                                            it.width,
                                            it.height
                                        )
                                    ) {
                                        newItems[Position2I(position.x - it.rootX, position.y - it.rootY)] = stack
                                    }
                                }
                                if (newItems.isNotEmpty()) {
                                    it.drag(newItems, event.cursor, event.type)
                                }
                            }
                        }
                        break
                    }
                }
            }
        }
    }
}