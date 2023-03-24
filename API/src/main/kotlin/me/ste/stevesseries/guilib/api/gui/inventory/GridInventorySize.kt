package me.ste.stevesseries.guilib.api.gui.inventory

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

enum class GridInventorySize(
    val width: Int,
    val height: Int,
    val inventoryType: InventoryType? = null
) {
    GENERIC_9X1(9, 1),
    GENERIC_9X2(9, 2),
    GENERIC_9X3(9, 3),
    GENERIC_9X4(9, 4),
    GENERIC_9X5(9, 5),
    GENERIC_9X6(9, 6),
    GENERIC_3X3(3, 3, InventoryType.DROPPER),
    HOPPER(5, 1, InventoryType.HOPPER);

    fun createInventory(server: Server, title: String, owner: InventoryHolder? = null) =
        if (this.inventoryType != null) {
            server.createInventory(owner, this.inventoryType, title)
        } else {
            server.createInventory(owner, this.width * this.height, title)
        }

    companion object {
        fun fromInventoryType(type: InventoryType, size: Int): GridInventorySize? {
            if (type == InventoryType.CHEST) {
                return values().find { it.width == 9 && it.height == size / it.width }
            }

            return values().find { it.inventoryType == type }
        }
    }
}