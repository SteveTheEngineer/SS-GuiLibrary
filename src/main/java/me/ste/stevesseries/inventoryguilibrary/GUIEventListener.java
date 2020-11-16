package me.ste.stevesseries.inventoryguilibrary;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

public class GUIEventListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(InventoryGUILibrary.getInstance().getPlayerGuis().containsKey(event.getPlayer().getUniqueId())) {
            InventoryGUILibrary.getInstance().getPlayerGuis().get(event.getPlayer().getUniqueId()).handleClosing(null);
            InventoryGUILibrary.getInstance().getPlayerGuis().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(InventoryGUILibrary.getInstance().getPlayerGuis().containsKey(event.getPlayer().getUniqueId())) {
            InventoryGUILibrary.getInstance().getPlayerGuis().get(event.getPlayer().getUniqueId()).handleClosing(event.getInventory());
            InventoryGUILibrary.getInstance().getPlayerGuis().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            if(InventoryGUILibrary.getInstance().getPlayerGuis().containsKey(event.getWhoClicked().getUniqueId()) && (!(event.getClickedInventory() instanceof PlayerInventory) || event.getClick().isShiftClick())) {
                event.setCancelled(true);
                InventoryGUILibrary.getInstance().getPlayerGuis().get(event.getWhoClicked().getUniqueId()).handleClick(event.getCurrentItem(), event.getClick(), event.getSlot(), event.getClickedInventory());
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            if(InventoryGUILibrary.getInstance().getPlayerGuis().containsKey(event.getWhoClicked().getUniqueId()) && !(event.getInventory() instanceof PlayerInventory)) {
                event.setCancelled(true);
            }
        }
    }
}