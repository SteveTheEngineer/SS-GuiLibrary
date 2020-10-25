package me.ste.stevesseries.inventoryguilibrary;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class InventoryGUILibrary extends JavaPlugin {
    private final Map<UUID, GUI> playerGuis = new HashMap<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GUIEventListener(), this);
    }

    @Override
    public void onDisable() {
        for(UUID uuid : this.getPlayerGuis().keySet()) {
            Objects.requireNonNull(this.getServer().getPlayer(uuid)).closeInventory();
        }
    }

    /**
     * Get the {@link InventoryGUILibrary} instance
     * @return {@link InventoryGUILibrary} instance
     */
    public static InventoryGUILibrary getInstance() {
        return InventoryGUILibrary.getPlugin(InventoryGUILibrary.class);
    }

    /**
     * Get the currently open inventory GUI
     * @param player target player
     * @return currently open inventory GUI or null if no GUI is open
     */
    public GUI getPlayerGUI(Player player) {
        return this.playerGuis.get(player.getUniqueId());
    }

    /**
     * Open an inventory GUI for the specified player
     * @param player target player
     * @param gui inventory GUI to open
     */
    public void openGUI(Player player, GUI gui) {
        Inventory inventory;
        if(gui.getInventoryType() == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(null, gui.getSize(), gui.getTitle());
        } else {
            inventory = Bukkit.createInventory(null, gui.getInventoryType(), gui.getTitle());
        }
        gui.handleOpening(inventory);
        gui.updateInventory(inventory);
        if(this.playerGuis.containsKey(player.getUniqueId())) {
            player.closeInventory();
        }
        this.playerGuis.put(player.getUniqueId(), gui);
        player.openInventory(inventory);
    }

    /**
     * Refresh the currently open GUI of the specified player
     * @param player target player
     */
    public void refreshGUI(Player player) {
        if(this.playerGuis.containsKey(player.getUniqueId())) {
            player.getOpenInventory().getTopInventory().clear();
            this.playerGuis.get(player.getUniqueId()).updateInventory(player.getOpenInventory().getTopInventory());
        }
    }

    /**
     * @deprecated for internal use only
     */
    @Deprecated
    public Map<UUID, GUI> getPlayerGuis() {
        return this.playerGuis;
    }
}
