package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class manages the GUIs. If you need to open a GUI for a player, this is the class that does that
 */
public class GUIManager {
    /**
     * All currently open GUIs. The key is the player unique id
     */
    public static final Map<UUID, Widget> GUIS = new HashMap<>();

    /**
     * Open a GUI for the given player
     * @param player player
     * @param gui GUI to open
     */
    public static void open(Player player, Widget gui) {
        GridInventory inventory = new GridInventory(gui.createInventory(), gui.getWidth(), gui.getHeight());
        gui.recurse(widget -> {
            widget.setPlayer(player);
            widget.onCreation(inventory);
            widget.render(inventory);
        });
        player.closeInventory();
        player.openInventory(inventory.getHandle());
        GUIManager.GUIS.put(player.getUniqueId(), gui);
    }

    /**
     * Check whether the player currently has a GUI open by the plugin
     * @param player target player
     * @return true, if the player has a GUI open
     */
    public static boolean isGuiOpen(Player player) {
        return GUIManager.GUIS.containsKey(player.getUniqueId());
    }

    /**
     * Get the currently open GUI of the player
     * @param player target player
     * @return the gui
     */
    public static Widget getGui(Player player) {
        return GUIManager.GUIS.get(player.getUniqueId());
    }

    /**
     * Re-render the currently open GUI of the player
     * @param player target player
     */
    public static void rerenderGui(Player player) {
        Widget gui = GUIManager.getGui(player);
        if(gui != null) {
            GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
            grid.clear();
            gui.recurse(widget -> widget.render(grid));
        }
    }
}