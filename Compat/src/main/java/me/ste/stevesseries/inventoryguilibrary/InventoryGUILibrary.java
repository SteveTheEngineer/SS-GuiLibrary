package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.guilib.api.GuiManager;
import me.ste.stevesseries.guilib.api.gui.GuiController;
import me.ste.stevesseries.guilib.compat.ControllerGUIWrapper;
import me.ste.stevesseries.guilib.compat.GUIAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public class InventoryGUILibrary {
    private static final InventoryGUILibrary INSTANCE = new InventoryGUILibrary();

    public static InventoryGUILibrary getInstance() {
        return INSTANCE;
    }

    /**
     * Get the currently open inventory GUI
     * @param player target player
     * @return currently open inventory GUI or null if no GUI is open
     */
    public GUI getPlayerGUI(Player player) {
        GuiController controller = GuiManager.Companion.getInstance().getGui(player);

        if (controller == null) {
            return null;
        }

        if (controller instanceof GUIAdapter) {
            return ((GUIAdapter) controller).getGui();
        }

        return new ControllerGUIWrapper(player, controller);
    }

    /**
     * Open an inventory GUI for the specified player
     * @param player target player
     * @param gui inventory GUI to open
     */
    public void openGUI(Player player, GUI gui) {
//        Inventory inventory;
//        gui.handleOpening(inventory);
//        gui.updateInventory(inventory);
//        if(this.playerGuis.containsKey(player.getUniqueId())) {
//            player.closeInventory();
//        }
//        this.playerGuis.put(player.getUniqueId(), gui);
//        player.openInventory(inventory);
        GuiManager.Companion.getInstance().openGui(player, new GUIAdapter(gui));
    }

    public void refreshGUI(Player player) {
        PlayerGUIS.INSTANCE.rerenderGui(player);
    }

    public Map<UUID, GUI> getPlayerGuis() {
        Map<UUID, GUI> map = new HashMap<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            GUI gui = this.getPlayerGUI(player);

            if (gui == null) {
                continue;
            }

            map.put(player.getUniqueId(), gui);
        }

        return map;
    }
}
