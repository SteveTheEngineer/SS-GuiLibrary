package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class InventoryGUILibrary extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GUIEventListener(), this);
    }

    @Override
    public void onDisable() {
        for(UUID uuid : GUIManager.GUIS.keySet()) {
            Objects.requireNonNull(this.getServer().getPlayer(uuid)).closeInventory();
        }
    }

    /**
     * @deprecated legacy API
     * @see GUIManager
     */
    @Deprecated
    public static InventoryGUILibrary getInstance() {
        return InventoryGUILibrary.getPlugin(InventoryGUILibrary.class);
    }

    /**
     * @deprecated legacy API
     * @see GUIManager
     */
    @Deprecated
    public GUI getPlayerGUI(Player player) {
        Widget gui = GUIManager.getGui(player);
        if(gui != null) {
            if(gui instanceof GUI) {
                return (GUI) gui;
            } else {
                return new GUIWidgetWrapper(gui);
            }
        } else {
            return null;
        }
    }

    /**
     * @deprecated legacy API
     * @see GUIManager
     */
    @Deprecated
    public void openGUI(Player player, GUI gui) {
        GUIManager.open(player, gui);
    }

    /**
     * @deprecated legacy API
     * @see GUIManager
     */
    @Deprecated
    public void refreshGUI(Player player) {
        Widget gui = GUIManager.GUIS.get(player.getUniqueId());
        if(gui != null) {
            GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
            grid.clear();
            gui.recurse(widget -> widget.render(grid));
        }
    }
}
